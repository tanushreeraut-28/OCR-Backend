package com.clideOffice.clideApp.common.ocr_project.sds.Section10.ServiceImpl;

import com.clideOffice.clideApp.common.ocr_project.sds.Section10.Service.Section10Service;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section10.Section10Item;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.Upload12And13And14Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.Upload7And8Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section10RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section10ResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.Section10Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadSdsResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class Section10ServiceImpl implements Section10Service {

    private final Section10Repository section10Repository;
    private final Upload12And13And14Repository repository;
    private final Upload7And8Repository upload7And8Repository;

    @Value("${tesseract.datapath}")
    private String tessDataPath;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "pdf", "doc", "docx", "txt",
            "jpg", "jpeg", "png", "bmp",
            "tiff", "tif", "gif", "webp"
    );

    // Upload 12, 13, 14
    @Override
    public UploadSdsResponseDto uploadSection12And13And14(
            UploadRequestDto uploadRequest
    ) {

        UploadSdsResponseDto uploadResponse =
                new UploadSdsResponseDto();

        try {

            MultipartFile uploadedFile =
                    uploadRequest.getFile();

            if (uploadedFile == null || uploadedFile.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            String uploadedFileName =
                    uploadedFile.getOriginalFilename();

            String uploadedFileType =
                    uploadedFile.getContentType();

            byte[] uploadedFileData =
                    uploadedFile.getBytes();

            String extension =
                    getFileExtension(uploadedFileName);

            if (!ALLOWED_TYPES.contains(extension.toLowerCase())) {

                throw new RuntimeException(
                        "Unsupported file type"
                );
            }

            String uploadedFileHash =
                    generateHash(uploadedFileData);

            if (repository.checkDuplicateFile(
                    uploadedFileHash
            ) > 0) {

                throw new RuntimeException(
                        "Duplicate file already uploaded"
                );
            }

            Long generatedSdsId =
                    repository.generateNextSdsId();

            String extractedOcrText =
                    cleanOcrText(runOCR(uploadedFile));

            String detectedSection =
                    uploadRequest.getSectionType();

            if (detectedSection == null
                    || detectedSection.isBlank()) {

                detectedSection =
                        detectSectionTypeFor12And13And14(
                                extractedOcrText
                        );
            }

            switch (detectedSection.trim().toUpperCase()) {

                // Section 12

                case "SECTION_12":

                    repository.insertSection12Data(

                            generatedSdsId,

                            extractSection12And13And14Block(
                                    extractedOcrText,
                                    "Toxicity",
                                    "Persistence and degradability"
                            ),

                            extractSection12And13And14Block(
                                    extractedOcrText,
                                    "Persistence and degradability",
                                    "Bioaccumulative potential"
                            ),

                            extractSection12And13And14Block(
                                    extractedOcrText,
                                    "Bioaccumulative potential",
                                    "Mobility in soil"
                            ),

                            extractSection12And13And14Block(
                                    extractedOcrText,
                                    "Mobility in soil",
                                    "Results of PBT"
                            ),

                            extractSection12And13And14Block(
                                    extractedOcrText,
                                    "Results of PBT",
                                    "Endocrine disrupting"
                            ),

                            extractSection12And13And14Block(
                                    extractedOcrText,
                                    "Endocrine disrupting",
                                    "Other adverse effects"
                            ),

                            extractSection12And13And14Block(
                                    extractedOcrText,
                                    "Other adverse effects",
                                    "Additional Information"
                            ),

                            extractSection12And13And14Block(
                                    extractedOcrText,
                                    "Additional Information",
                                    null
                            ),

                            extractedOcrText,

                            uploadedFileData,
                            uploadedFileName,
                            uploadedFileType,
                            uploadedFileHash
                    );

                    break;

                // Section 13

                case "SECTION_13":

                    repository.insertSection13Data(

                            generatedSdsId,

                            safeValue(
                                    extractSection12And13And14Block(
                                            extractedOcrText,
                                            "13.1",
                                            "13.1.1"
                                    )
                            ),

                            safeValue(
                                    extractSection12And13And14Block(
                                            extractedOcrText,
                                            "13.1.1",
                                            "13.1.2"
                                    )
                            ),

                            safeValue(
                                    extractSection12And13And14Block(
                                            extractedOcrText,
                                            "13.1.2",
                                            "13.1.3"
                                    )
                            ),

                            safeValue(
                                    extractSection12And13And14Block(
                                            extractedOcrText,
                                            "13.1.3",
                                            "13.1.4"
                                    )
                            ),

                            safeValue(
                                    extractSection12And13And14Block(
                                            extractedOcrText,
                                            "13.1.4",
                                            "13.1.5"
                                    )
                            ),

                            safeValue(
                                    extractSection12And13And14Block(
                                            extractedOcrText,
                                            "13.1.5",
                                            "Additional Information"
                                    )
                            ),

                            safeValue(
                                    extractSection12And13And14Block(
                                            extractedOcrText,
                                            "Additional Information",
                                            null
                                    )
                            ),

                            uploadedFileData,
                            uploadedFileName,
                            uploadedFileType,
                            uploadedFileHash
                    );

                    break;

                // Section 14

                case "SECTION_14":

                    repository.insertSection14Data(

                            generatedSdsId,

                            safeValue(
                                    extractSection12And13And14Block(
                                            extractedOcrText,
                                            "14.1",
                                            "14.2"
                                    )
                            ),

                            safeValue(
                                    extractSection12And13And14Block(
                                            extractedOcrText,
                                            "14.2",
                                            "14.3"
                                    )
                            ),

                            safeValue(
                                    extractSection12And13And14Block(
                                            extractedOcrText,
                                            "14.3",
                                            "14.4"
                                    )
                            ),

                            safeValue(
                                    extractSection12And13And14Block(
                                            extractedOcrText,
                                            "14.4",
                                            "14.5"
                                    )
                            ),

                            safeValue(
                                    extractSection12And13And14Block(
                                            extractedOcrText,
                                            "14.5",
                                            "14.6"
                                    )
                            ),

                            safeValue(
                                    extractSection12And13And14Block(
                                            extractedOcrText,
                                            "14.6",
                                            "14.7"
                                    )
                            ),

                            safeValue(
                                    extractSection12And13And14Block(
                                            extractedOcrText,
                                            "14.7",
                                            "Additional Information"
                                    )
                            ),

                            safeValue(
                                    extractSection12And13And14Block(
                                            extractedOcrText,
                                            "Additional Information",
                                            null
                                    )
                            ),

                            uploadedFileData,
                            uploadedFileName,
                            uploadedFileType,
                            uploadedFileHash
                    );

                    break;

                default:

                    throw new RuntimeException(
                            "Invalid section type"
                    );
            }

            uploadResponse.setStatus("SUCCESS");
            uploadResponse.setSdsId(generatedSdsId);
            uploadResponse.setVersion(1);
            uploadResponse.setMessage(
                    "Uploaded successfully"
            );

            return uploadResponse;

        } catch (Exception exception) {

            exception.printStackTrace();

            uploadResponse.setStatus("FAILED");
            uploadResponse.setMessage(
                    "Upload failed : "
                            + exception.getMessage()
            );

            return uploadResponse;
        }
    }


// Detect section type
    private String detectSectionTypeFor12And13And14(
            String extractedText
    ) {

        try {

            if (extractedText == null
                    || extractedText.isBlank()) {

                return "UNKNOWN";
            }

            String lowerText =
                    extractedText.toLowerCase();

            if (lowerText.contains("section 12")
                    || lowerText.contains("ecological information")
                    || lowerText.contains("toxicity")
                    || lowerText.contains("persistence and degradability")
                    || lowerText.contains("bioaccumulative potential")) {

                return "SECTION_12";
            }

            if (lowerText.contains("section 13")
                    || lowerText.contains("disposal considerations")
                    || lowerText.contains("waste treatment methods")
                    || lowerText.contains("product disposal")
                    || lowerText.contains("packaging disposal")) {

                return "SECTION_13";
            }

            if (lowerText.contains("section 14")
                    || lowerText.contains("transport information")
                    || lowerText.contains("un number")
                    || lowerText.contains("proper shipping name")
                    || lowerText.contains("packing group")) {

                return "SECTION_14";
            }

            return "UNKNOWN";

        } catch (Exception exception) {

            exception.printStackTrace();

            return "UNKNOWN";
        }
    }


// OCR helper
    private String runOCR(
            MultipartFile uploadedFile
    ) {

        try {

            String originalFileName =
                    uploadedFile.getOriginalFilename();

            if (originalFileName == null) {
                throw new RuntimeException("Invalid file");
            }

            String extension =
                    originalFileName.substring(
                            originalFileName.lastIndexOf(".") + 1
                    ).toLowerCase();

            if (!ALLOWED_TYPES.contains(extension)) {

                throw new RuntimeException(
                        "Only PDF, DOCX, TXT, JPG, JPEG, PNG, BMP, TIFF, GIF and WEBP files are supported"
                );
            }

            File temporaryFile =
                    File.createTempFile(
                            "ocr_upload_",
                            "." + extension
                    );

            uploadedFile.transferTo(temporaryFile);

            Tesseract tesseract =
                    new Tesseract();

            tesseract.setDatapath(tessDataPath);

            String extractedText = "";

            if (extension.equals("pdf")) {

                StringBuilder stringBuilder =
                        new StringBuilder();

                PDDocument pdfDocument =
                        PDDocument.load(temporaryFile);

                PDFRenderer pdfRenderer =
                        new PDFRenderer(pdfDocument);

                for (int page = 0;
                     page < pdfDocument.getNumberOfPages();
                     page++) {

                    BufferedImage bufferedImage =
                            pdfRenderer.renderImageWithDPI(
                                    page,
                                    300
                            );

                    stringBuilder.append(
                            tesseract.doOCR(bufferedImage)
                    );

                    stringBuilder.append("\n");
                }

                pdfDocument.close();

                extractedText =
                        stringBuilder.toString();
            }

            else if (extension.equals("txt")) {

                extractedText =
                        Files.readString(
                                temporaryFile.toPath()
                        );
            }

            else if (extension.equals("docx")) {

                FileInputStream fileInputStream =
                        new FileInputStream(temporaryFile);

                XWPFDocument document =
                        new XWPFDocument(fileInputStream);

                XWPFWordExtractor extractor =
                        new XWPFWordExtractor(document);

                extractedText =
                        extractor.getText();

                extractor.close();
                document.close();
                fileInputStream.close();
            }

            else {

                BufferedImage bufferedImage =
                        ImageIO.read(temporaryFile);

                if (bufferedImage == null) {

                    throw new RuntimeException(
                            "Unsupported image format"
                    );
                }

                extractedText =
                        tesseract.doOCR(bufferedImage);
            }

            temporaryFile.delete();

            return extractedText;

        } catch (Exception exception) {

            exception.printStackTrace();

            throw new RuntimeException(
                    "OCR failed : "
                            + exception.getMessage()
            );
        }
    }


// Clean OCR text
    private String cleanOcrText(String extractedText
    ) {

        try {

            if (extractedText == null) {
                return "";
            }

            return extractedText
                    .replaceAll("[^\\x00-\\x7F]", " ")
                    .replace("\n", " ")
                    .replace("\r", " ")
                    .replaceAll("\\s+", " ")
                    .trim();

        } catch (Exception exception) {

            exception.printStackTrace();

            return "";
        }
    }


// Generate file hash
    private String generateHash(byte[] uploadedFileData
    ) {
        try {

            MessageDigest messageDigest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hashBytes =
                    messageDigest.digest(uploadedFileData);

            StringBuilder stringBuilder =
                    new StringBuilder();

            for (byte singleByte : hashBytes) {

                stringBuilder.append(
                        String.format(
                                "%02x",
                                singleByte
                        )
                );
            }

            return stringBuilder.toString();

        } catch (Exception exception) {

            exception.printStackTrace();

            throw new RuntimeException(
                    "Hash generation failed"
            );
        }
    }


// Extract section block
private String extractSection12And13And14Block(
        String completeText,
        String startKeyword,
        String endKeyword
) {

    try {

        if (completeText == null
                || completeText.isBlank()) {

            return null;
        }

        String normalizedText =
                completeText
                        .replace("\n", " ")
                        .replace("\r", " ")
                        .replaceAll("\\s+", " ")
                        .trim();

        String lowerCaseText =
                normalizedText.toLowerCase();

        String start =
                startKeyword.toLowerCase().trim();

        int startIndex =
                lowerCaseText.indexOf(start);

        if (startIndex == -1) {

            start =
                    start.replace(".", "");

            startIndex =
                    lowerCaseText
                            .replace(".", "")
                            .indexOf(start);
        }

        if (startIndex == -1) {
            return null;
        }

        startIndex =
                startIndex + startKeyword.length();

        int endIndex;

        if (endKeyword != null) {

            String end =
                    endKeyword.toLowerCase().trim();

            endIndex =
                    lowerCaseText.indexOf(
                            end,
                            startIndex
                    );

            if (endIndex == -1) {

                end =
                        end.replace(".", "");

                endIndex =
                        lowerCaseText
                                .replace(".", "")
                                .indexOf(
                                        end,
                                        startIndex
                                );
            }

            if (endIndex == -1) {
                endIndex =
                        normalizedText.length();
            }

        } else {

            endIndex =
                    normalizedText.length();
        }

        String extractedValue =
                normalizedText.substring(
                        startIndex,
                        endIndex
                ).trim();

        return safeValue(extractedValue);

    } catch (Exception exception) {

        exception.printStackTrace();

        return null;
    }
}


// Safe value
    private String safeValue(String value
    ) {

        try {

            if (value == null
                    || value.isBlank()) {

                return null;
            }

            return value.trim();

        } catch (Exception exception) {

            exception.printStackTrace();

            return null;
        }
    }


// Get file extension
    private String getFileExtension(String fileName
    ) {

        try {

            if (fileName == null
                    || !fileName.contains(".")) {

                return "";
            }

            return fileName.substring(
                    fileName.lastIndexOf(".") + 1
            );

        } catch (Exception exception) {

            exception.printStackTrace();

            return "";
        }
    }




    // ================= POST =================
    @Override
    public Section10ResponseDTO addItem(Section10RequestDTO requestDTO) {

        Section10Item entity = Section10Item.builder()
                .sdsId(requestDTO.getSdsId())
                .itemName(requestDTO.getItemName())
                .information(requestDTO.getInformation())
                .remarks(requestDTO.getRemarks())
                .isDeleted(false)
                .build();

        Section10Item saved = section10Repository.save(entity);

        Section10ResponseDTO response = new Section10ResponseDTO();
        response.setId(saved.getId());
        response.setSdsId(saved.getSdsId());
        response.setItemName(saved.getItemName());
        response.setInformation(saved.getInformation());
        response.setRemarks(saved.getRemarks());

        return response;
    }

    // ================= DELETE (SOFT DELETE) =================
    @Override
    public void deleteItem(Long id) {

        Section10Item entity = section10Repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section10 item not found with id: " + id));

        entity.setIsDeleted(true); // ✅ Soft delete
        section10Repository.save(entity);
    }


    // UPLOAD SECTION 7 & 8
    @Override
    @Transactional
    public UploadSdsResponseDto uploadSection7And8(
            UploadRequestDto request
    ) {

        UploadSdsResponseDto response =
                new UploadSdsResponseDto();

        try {

            MultipartFile file =
                    request.getFile();

            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            validateSection7And8File(file);

            String fileName =
                    file.getOriginalFilename();

            String fileType =
                    file.getContentType();

            byte[] fileData =
                    file.getBytes();

            String fileHash =
                    generateSection7And8Hash(fileData);

            if (upload7And8Repository
                    .checkDuplicateFile(fileHash) > 0) {

                response.setStatus("DUPLICATE");
                response.setMessage(
                        "Duplicate file already uploaded"
                );

                return response;
            }

            Long sdsId =
                    upload7And8Repository
                            .generateNextSdsId();

            String extractedText =
                    cleanSection7And8OcrText(
                            runSection7And8OCR(file)
                    );

            String sectionType =
                    detectSection7And8Type(
                            extractedText
                    );

            switch (sectionType) {

                case "SECTION_7":

                    upload7And8Repository
                            .insertSafeHandlingData(

                                    sdsId,

                                    extractSection7And8Block(
                                            extractedText,
                                            "Precautions for safe handling",
                                            "Conditions for safe storage"
                                    ),

                                    extractSection7And8Block(
                                            extractedText,
                                            "general occupational hygiene",
                                            "Conditions for safe handling"
                                    ),

                                    extractSection7And8Block(
                                            extractedText,
                                            "Protective measures",
                                            "Advice on general occupational hygiene"
                                    ),

                                    extractSection7And8Block(
                                            extractedText,
                                            "Measures to prevent fire",
                                            "Measures to prevent aerosol"
                                    ),

                                    fileData,
                                    fileName,
                                    fileType,
                                    fileHash
                            );

                    upload7And8Repository
                            .insertSafeStorageData(

                                    sdsId,

                                    extractSection7And8Block(
                                            extractedText,
                                            "Technical measures",
                                            "Storage conditions"
                                    ),

                                    extractSection7And8Block(
                                            extractedText,
                                            "Storage conditions",
                                            "Incompatible materials"
                                    ),

                                    extractSection7And8Block(
                                            extractedText,
                                            "Incompatible materials",
                                            "Packaging materials"
                                    ),

                                    extractSection7And8Block(
                                            extractedText,
                                            "Packaging materials",
                                            "Storage temperature"
                                    ),

                                    extractSection7And8Block(
                                            extractedText,
                                            "Storage temperature",
                                            "Storage area"
                                    ),

                                    extractSection7And8Block(
                                            extractedText,
                                            "Storage area",
                                            "Additional information"
                                    ),

                                    extractSection7And8Block(
                                            extractedText,
                                            "well-ventilated area",
                                            "Additional information"
                                    ),

                                    extractSection7And8Block(
                                            extractedText,
                                            "Additional information",
                                            "Specific end use"
                                    ),

                                    fileData,
                                    fileName,
                                    fileType,
                                    fileHash
                            );

                    upload7And8Repository
                            .insertSpecificEndUseData(

                                    sdsId,

                                    extractSection7And8Block(
                                            extractedText,
                                            "Specific end use",
                                            null
                                    ),

                                    fileData,
                                    fileName,
                                    fileType,
                                    fileHash
                            );

                    break;

                case "SECTION_8":

                    // SECTION 8 logic here later

                    break;

                default:
                    throw new RuntimeException(
                            "Invalid section detected"
                    );
            }

            response.setStatus("SUCCESS");
            response.setSdsId(sdsId);
            response.setVersion(1);
            response.setMessage("Uploaded successfully");

            return response;

        } catch (Exception exception) {

            exception.printStackTrace();

            response.setStatus("FAILED");
            response.setMessage(
                    "Upload failed : "
                            + exception.getMessage()
            );

            return response;
        }
    }


    // ==========================================================
// SECTION 7 & 8 HELPER METHODS
// ==========================================================

// ==========================================================
// VALIDATE FILE
// ==========================================================

    private void validateSection7And8File(
            MultipartFile file
    ) {

        try {

            String fileName =
                    file.getOriginalFilename();

            if (fileName == null
                    || !fileName.contains(".")) {

                throw new RuntimeException(
                        "Invalid file name"
                );
            }

            String extension =
                    fileName.substring(
                            fileName.lastIndexOf(".") + 1
                    ).toLowerCase();

            if (!ALLOWED_TYPES.contains(extension)) {

                throw new RuntimeException(
                        "Unsupported file type"
                );
            }

        } catch (Exception exception) {

            exception.printStackTrace();

            throw new RuntimeException(
                    "File validation failed"
            );
        }
    }

// ==========================================================
// RUN OCR
// ==========================================================

    private String runSection7And8OCR(
            MultipartFile file
    ) {

        try {

            String fileName =
                    file.getOriginalFilename();

            if (fileName == null) {
                throw new RuntimeException(
                        "Invalid file"
                );
            }

            String extension =
                    fileName.substring(
                            fileName.lastIndexOf(".") + 1
                    ).toLowerCase();

            if (extension.equals("pdf")) {

                return readSection7And8Pdf(file);
            }

            if (extension.equals("txt")) {

                return new String(file.getBytes());
            }

            if (extension.equals("doc")
                    || extension.equals("docx")) {

                return readSection7And8Word(file);
            }

            return readSection7And8Image(file);

        } catch (Exception exception) {

            exception.printStackTrace();

            throw new RuntimeException(
                    "OCR failed : "
                            + exception.getMessage()
            );
        }
    }

// ==========================================================
// IMAGE OCR
// ==========================================================

    private String readSection7And8Image(
            MultipartFile file
    ) {

        try {

            BufferedImage image =
                    ImageIO.read(
                            file.getInputStream()
                    );

            if (image == null) {

                throw new RuntimeException(
                        "Unable to read image"
                );
            }

            Tesseract tesseract =
                    new Tesseract();

            tesseract.setDatapath(
                    tessDataPath
            );

            tesseract.setLanguage("eng");

            return tesseract.doOCR(image);

        } catch (Exception exception) {

            exception.printStackTrace();

            throw new RuntimeException(
                    "Image OCR failed"
            );
        }
    }

// ==========================================================
// PDF OCR
// ==========================================================

    private String readSection7And8Pdf(
            MultipartFile file
    ) {

        try {

            StringBuilder extractedText =
                    new StringBuilder();

            PDDocument document =
                    PDDocument.load(
                            file.getInputStream()
                    );

            PDFRenderer renderer =
                    new PDFRenderer(document);

            Tesseract tesseract =
                    new Tesseract();

            tesseract.setDatapath(
                    tessDataPath
            );

            tesseract.setLanguage("eng");

            for (int page = 0;
                 page < document.getNumberOfPages();
                 page++) {

                BufferedImage image =
                        renderer.renderImageWithDPI(
                                page,
                                300
                        );

                extractedText.append(
                        tesseract.doOCR(image)
                );

                extractedText.append("\n");
            }

            document.close();

            return extractedText.toString();

        } catch (Exception exception) {

            exception.printStackTrace();

            throw new RuntimeException(
                    "PDF OCR failed"
            );
        }
    }

// ==========================================================
// WORD OCR
// ==========================================================

    private String readSection7And8Word(
            MultipartFile file
    ) {

        try {

            XWPFDocument document =
                    new XWPFDocument(
                            file.getInputStream()
                    );

            XWPFWordExtractor extractor =
                    new XWPFWordExtractor(
                            document
                    );

            String extractedText =
                    extractor.getText();

            extractor.close();

            document.close();

            return extractedText;

        } catch (Exception exception) {

            exception.printStackTrace();

            throw new RuntimeException(
                    "Word OCR failed"
            );
        }
    }

// ==========================================================
// CLEAN OCR TEXT
// ==========================================================

    private String cleanSection7And8OcrText(
            String text
    ) {

        try {

            if (text == null) {
                return "";
            }

            return text
                    .replaceAll(
                            "[^\\x00-\\x7F]",
                            " "
                    )
                    .replace("\n", " ")
                    .replace("\r", " ")
                    .replaceAll(
                            "\\s+",
                            " "
                    )
                    .trim();

        } catch (Exception exception) {

            exception.printStackTrace();

            return "";
        }
    }

// ==========================================================
// DETECT SECTION TYPE
// ==========================================================

    private String detectSection7And8Type(
            String text
    ) {

        try {

            if (text == null
                    || text.isBlank()) {

                return "UNKNOWN";
            }

            String lowerText =
                    text.toLowerCase();

            // SECTION 7

            if (lowerText.contains("section 7")
                    || lowerText.contains("handling and storage")
                    || lowerText.contains("precautions for safe handling")
                    || lowerText.contains("conditions for safe storage")
                    || lowerText.contains("specific end use")) {

                return "SECTION_7";
            }

            // SECTION 8

            if (lowerText.contains("section 8")
                    || lowerText.contains("exposure controls")
                    || lowerText.contains("personal protection")
                    || lowerText.contains("dnels")
                    || lowerText.contains("pnecs")
                    || lowerText.contains("control parameters")) {

                return "SECTION_8";
            }

            return "UNKNOWN";

        } catch (Exception exception) {

            exception.printStackTrace();

            return "UNKNOWN";
        }
    }

// ==========================================================
// EXTRACT BLOCK
// ==========================================================

    private String extractSection7And8Block(
            String completeText,
            String startKeyword,
            String endKeyword
    ) {

        try {

            if (completeText == null
                    || completeText.isBlank()) {

                return null;
            }

            String normalizedText =
                    completeText
                            .replace("\n", " ")
                            .replace("\r", " ")
                            .replaceAll(
                                    "\\s+",
                                    " "
                            )
                            .trim();

            String lowerText =
                    normalizedText.toLowerCase();

            int startIndex =
                    lowerText.indexOf(
                            startKeyword.toLowerCase()
                    );

            if (startIndex == -1) {

                return null;
            }

            startIndex =
                    startIndex
                            + startKeyword.length();

            int endIndex;

            if (endKeyword != null) {

                endIndex =
                        lowerText.indexOf(
                                endKeyword.toLowerCase(),
                                startIndex
                        );

                if (endIndex == -1) {

                    endIndex =
                            normalizedText.length();
                }

            } else {

                endIndex =
                        normalizedText.length();
            }

            String extractedValue =
                    normalizedText.substring(
                            startIndex,
                            endIndex
                    ).trim();

            return safeSection7And8Value(
                    extractedValue
            );

        } catch (Exception exception) {

            exception.printStackTrace();

            return null;
        }
    }

// ==========================================================
// SAFE VALUE
// ==========================================================

    private String safeSection7And8Value(
            String value
    ) {

        try {

            if (value == null
                    || value.isBlank()) {

                return null;
            }

            return value.trim();

        } catch (Exception exception) {

            exception.printStackTrace();

            return null;
        }
    }

// ==========================================================
// GENERATE HASH
// ==========================================================

    private String generateSection7And8Hash(
            byte[] fileData
    ) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance(
                            "SHA-256"
                    );

            byte[] hashBytes =
                    digest.digest(fileData);

            StringBuilder stringBuilder =
                    new StringBuilder();

            for (byte singleByte : hashBytes) {

                stringBuilder.append(
                        String.format(
                                "%02x",
                                singleByte
                        )
                );
            }

            return stringBuilder.toString();

        } catch (Exception exception) {

            exception.printStackTrace();

            throw new RuntimeException(
                    "Hash generation failed"
            );
        }
    }

// ==========================================================
// GET FILE EXTENSION
// ==========================================================

    private String getSection7And8FileExtension(
            String fileName
    ) {

        try {

            if (fileName == null
                    || !fileName.contains(".")) {

                return "";
            }

            return fileName.substring(
                    fileName.lastIndexOf(".") + 1
            );

        } catch (Exception exception) {

            exception.printStackTrace();

            return "";
        }
    }

}