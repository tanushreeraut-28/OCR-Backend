package com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import com.clideOffice.clideApp.common.ocr_project.util.BucketContextHolder;
import com.clideOffice.clideApp.common.ocr_project.util.DatabaseContextHolder;

import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.UploadProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.UploadRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.service.UploadService;

import lombok.RequiredArgsConstructor;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final UploadRepository uploadRepository;
    private final AmazonS3 amazonS3;

    @Value("${amazon.url}")
    private String amazonUrl;

    @Value("${tesseract.datapath}")
    private String tessDataPath;

    private static final List<String> ALLOWED_TYPES = List.of(
            "pdf","doc","docx","txt",
            "jpg","jpeg","png","bmp",
            "tiff","tif","gif","webp","heic"
    );

    @Override
    public UploadResponseDto uploadSds(UploadRequestDto request) {

        UploadResponseDto response = new UploadResponseDto();

        try {

            /*
            =====================
            SET DATABASE CONTEXT
            =====================
            */

            DatabaseContextHolder.setDatabase("qaclide");

            MultipartFile file = request.getFile();

            if(file == null || file.isEmpty()){
                throw new RuntimeException("File is empty");
            }

            validateFileType(file);

            /*
            =====================
            UPLOAD FILE TO S3
            =====================
            */

            String bucketName = BucketContextHolder.getBucketName(
                    DatabaseContextHolder.getDatabase()
            );

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            String filePath = "sds/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(
                    bucketName,
                    filePath,
                    file.getInputStream(),
                    metadata
            );

            String fileUrl = amazonUrl + "/" + bucketName + "/" + filePath;

            /*
            =====================
            RUN OCR
            =====================
            */

            String rawText = runOCR(file);

            double confidenceScore = 0.90;

            String productIdentifier = extractProductIdentifier(rawText);

            /*
            =====================
            DUPLICATE DETECTION
            =====================
            */

            UploadProjection duplicate = uploadRepository.findDuplicateSds(productIdentifier);

            if(duplicate != null){

                uploadRepository.logDuplicate(
                        productIdentifier,
                        duplicate.getSdsId(),
                        "DUPLICATE_DETECTED"
                );

                response.setStatus("DUPLICATE");
                response.setSdsId(duplicate.getSdsId());
                response.setMessage("Duplicate SDS detected");

                return response;
            }

            /*
            =====================
            INSERT SDS MASTER
            =====================
            */

            uploadRepository.insertSdsMaster(
                    productIdentifier,
                    null,
                    null,
                    1L
            );

            Long sdsId = uploadRepository.getSdsId(productIdentifier);

            /*
            =====================
            INSERT VERSION
            =====================
            */

            uploadRepository.insertVersion(
                    sdsId,
                    1,
                    fileUrl,
                    "Initial Upload",
                    1L
            );

            Long versionId = uploadRepository.getVersionId(sdsId);

            /*
            =====================
            INSERT SECTION 1
            =====================
            */

            uploadRepository.insertSection1(
                    sdsId,
                    versionId,
                    productIdentifier,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "OCR"
            );

            /*
            =====================
            SAVE OCR RESULT
            =====================
            */

            uploadRepository.saveOcrResult(
                    sdsId,
                    versionId,
                    rawText,
                    confidenceScore
            );

            /*
            =====================
            AUDIT LOG
            =====================
            */

            uploadRepository.insertAuditLog(
                    sdsId,
                    "UPLOAD",
                    1L,
                    "SDS uploaded successfully"
            );

            response.setStatus("SUCCESS");
            response.setSdsId(sdsId);
            response.setVersion(1);
            response.setFileUrl(fileUrl);
            response.setMessage("SDS uploaded successfully");

            return response;

        } catch (Exception e){

            throw new RuntimeException(
                    "Upload failed: " + e.getMessage()
            );

        } finally {

            /*
            =====================
            CLEAR DATABASE CONTEXT
            =====================
            */

            DatabaseContextHolder.clear();
        }
    }

    /*
    =============================
    FILE TYPE VALIDATION
    =============================
    */

    private void validateFileType(MultipartFile file){

        String name = file.getOriginalFilename();

        if(name == null){
            throw new RuntimeException("Invalid file name");
        }

        String extension = name.substring(name.lastIndexOf(".")+1).toLowerCase();

        if(!ALLOWED_TYPES.contains(extension)){
            throw new RuntimeException("Unsupported file type");
        }
    }

    /*
    =============================
    OCR ENGINE
    =============================
    */

    private String runOCR(MultipartFile file) throws Exception {

        String extension = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".")+1)
                .toLowerCase();

        if(extension.equals("pdf")){
            return readPdf(file);
        }

        if(extension.equals("txt")){
            return new String(file.getBytes());
        }

        if(extension.equals("doc") || extension.equals("docx")){
            return readWord(file);
        }

        return readImage(file);
    }

    /*
    =============================
    IMAGE OCR
    =============================
    */

    private String readImage(MultipartFile file) throws Exception {

        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessDataPath);

        BufferedImage image = ImageIO.read(file.getInputStream());

        return tesseract.doOCR(image);
    }

    /*
    =============================
    PDF OCR
    =============================
    */

    private String readPdf(MultipartFile file) throws Exception {

        PDDocument document = PDDocument.load(file.getInputStream());

        PDFRenderer renderer = new PDFRenderer(document);

        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessDataPath);

        StringBuilder text = new StringBuilder();

        for(int i=0;i<document.getNumberOfPages();i++){

            BufferedImage image = renderer.renderImageWithDPI(i,300);

            text.append(
                    tesseract.doOCR(image)
            );
        }

        document.close();

        return text.toString();
    }

    /*
    =============================
    WORD FILE READER
    =============================
    */

    private String readWord(MultipartFile file) throws Exception {

        XWPFDocument doc = new XWPFDocument(file.getInputStream());

        StringBuilder text = new StringBuilder();

        doc.getParagraphs().forEach(
                p -> text.append(p.getText()).append("\n")
        );

        doc.close();

        return text.toString();
    }

    /*
    =============================
    PRODUCT IDENTIFIER EXTRACTION
    =============================
    */

    private String extractProductIdentifier(String text){

        Pattern pattern = Pattern.compile("Product Identifier[:\\- ]*(.*)");

        Matcher matcher = pattern.matcher(text);

        if(matcher.find()){
            return matcher.group(1).trim();
        }

        return "UNKNOWN_PRODUCT";
    }
}