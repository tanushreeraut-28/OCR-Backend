package com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
import com.amazonaws.services.s3.model.PutObjectRequest;

import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.UploadProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.UploadRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.service.UploadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadServiceImpl implements UploadService {

    private final UploadRepository uploadRepository;
    private final AmazonS3 amazonS3;

    @Value("${amazon.url}")
    private String amazonUrl;

    @Value("${aws.s3.bucket.qaclide}")
    private String bucketName;

    @Value("${tesseract.datapath}")
    private String tessDataPath;

    private static final List<String> ALLOWED_TYPES = List.of(
            "pdf","doc","docx","txt",
            "jpg","jpeg","png","bmp",
            "tiff","tif","gif","webp"
    );

    @Override
    public UploadResponseDto uploadSds(UploadRequestDto request) {

        UploadResponseDto response = new UploadResponseDto();

        try {

            MultipartFile file = request.getFile();

            if(file == null || file.isEmpty()){
                throw new RuntimeException("File is empty");
            }

            validateFileType(file);

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String s3Key = "sds/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(
                    new PutObjectRequest(
                            bucketName,
                            s3Key,
                            new ByteArrayInputStream(file.getBytes()),
                            metadata
                    )
            );

            String fileUrl = amazonS3.getUrl(bucketName, s3Key).toString();

            // OCR
            String rawText = runOCR(file);

            log.info("===== OCR EXTRACTED TEXT START =====");
            log.info(rawText);
            log.info("===== OCR EXTRACTED TEXT END =====");

            String cleanedText = cleanOcrText(rawText);

            log.info("===== CLEANED OCR TEXT =====");
            log.info(cleanedText);

            double confidenceScore = 0.90;

            String productIdentifier = extractProductIdentifier(cleanedText);

            log.info("Extracted Product Identifier: {}", productIdentifier);

            if(productIdentifier == null || productIdentifier.isBlank()){
                response.setStatus("OCR_FAILED");
                response.setMessage("Product Identifier not detected");
                response.setFileUrl(fileUrl);
                return response;
            }

            // DUPLICATE CHECK
            UploadProjection duplicate =
                    uploadRepository.findDuplicateSds(productIdentifier);

            if(duplicate != null){

                response.setStatus("DUPLICATE");
                response.setSdsId(duplicate.getSdsId());
                response.setMessage("Duplicate SDS detected for product: " + productIdentifier);
                response.setFileUrl(fileUrl);

                return response;
            }

            // INSERT SDS MASTER
            uploadRepository.insertSdsMaster(
                    productIdentifier,
                    null,
                    null,
                    1L
            );

            Long sdsId = uploadRepository.getSdsId(productIdentifier);

            uploadRepository.insertVersion(
                    sdsId,
                    1,
                    fileUrl,
                    "Initial Upload",
                    1L
            );

            Long versionId = uploadRepository.getVersionId(sdsId);

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

            uploadRepository.saveOcrResult(
                    sdsId,
                    versionId,
                    rawText,
                    confidenceScore
            );

            response.setStatus("SUCCESS");
            response.setSdsId(sdsId);
            response.setVersion(1);
            response.setFileUrl(fileUrl);
            response.setMessage("SDS uploaded successfully");

            return response;

        } catch (Exception e){

            log.error("SDS Upload Error", e);

            throw new RuntimeException(
                    "Upload failed: " + e.getMessage()
            );
        }
    }

    private void validateFileType(MultipartFile file){

        String name = file.getOriginalFilename();

        if(name == null){
            throw new RuntimeException("Invalid file name");
        }

        if(!name.contains(".")){
            throw new RuntimeException("File extension missing");
        }

        String extension =
                name.substring(name.lastIndexOf(".")+1).toLowerCase();

        if(!ALLOWED_TYPES.contains(extension)){
            throw new RuntimeException("Unsupported file type");
        }
    }

    private String runOCR(MultipartFile file) throws Exception {

        String name = file.getOriginalFilename();

        String extension =
                name.substring(name.lastIndexOf(".")+1).toLowerCase();

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

    private String readImage(MultipartFile file) throws Exception {

        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessDataPath);

        BufferedImage image = ImageIO.read(file.getInputStream());

        return tesseract.doOCR(image);
    }

    private String readPdf(MultipartFile file) throws Exception {

        StringBuilder text = new StringBuilder();

        try(PDDocument document = PDDocument.load(file.getInputStream())){

            PDFRenderer renderer = new PDFRenderer(document);

            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath(tessDataPath);

            for(int i=0;i<document.getNumberOfPages();i++){

                BufferedImage image =
                        renderer.renderImageWithDPI(i,300);

                text.append(
                        tesseract.doOCR(image)
                ).append("\n");
            }
        }

        return text.toString();
    }

    private String readWord(MultipartFile file) throws Exception {

        StringBuilder text = new StringBuilder();

        try(XWPFDocument doc =
                new XWPFDocument(file.getInputStream())){

            doc.getParagraphs().forEach(
                    p -> text.append(p.getText()).append("\n")
            );
        }

        return text.toString();
    }

    private String cleanOcrText(String text){

        if(text == null){
            return "";
        }

        return text
                .replaceAll("[^\\x00-\\x7F]", " ")
                .replaceAll("[*]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String extractProductIdentifier(String text){

        if(text == null || text.isBlank()){
            return null;
        }

        // Primary match
        Pattern p1 = Pattern.compile(
                "(Product Identifier|Product Name|Chemical Name|Substance Name)\\s*[:\\-]\\s*([A-Za-z0-9()\\- ]{2,80})",
                Pattern.CASE_INSENSITIVE
        );

        Matcher m1 = p1.matcher(text);

        if(m1.find()){
            String value = m1.group(2).trim();
            value = value.split("Other Name")[0].trim();
            return value;
        }

        // Fallback
        Pattern p2 = Pattern.compile(
                "(Other Name of Identification|Other Identification|Synonym)\\s*[:\\-]\\s*([A-Za-z0-9()\\- ]{2,80})",
                Pattern.CASE_INSENSITIVE
        );

        Matcher m2 = p2.matcher(text);

        if(m2.find()){
            String value = m2.group(2).trim();
            value = value.replaceAll("[()]", "");
            return value;
        }

        return null;
    }
}