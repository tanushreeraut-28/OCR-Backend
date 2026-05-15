package com.clideOffice.clideApp.common.ocr_project.sds.upload.serviceImpl;

import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.UploadProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.Upload9_10_11Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.UploadSdsRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.Upload5And6Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadSdsResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.upload.service.UploadService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.clideOffice.clideApp.common.ocr_project.sds.Section3.serviceImpl.Section3ServiceImpl.generateHash;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final UploadSdsRepository uploadRepository;
    private final Upload5And6Repository repository;
    private final Upload9_10_11Repository upload9_10_11Repository;

    @Value("${tesseract.datapath}")
    private String tessDataPath;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "pdf", "doc", "docx", "txt",
            "jpg", "jpeg", "png", "bmp",
            "tiff", "tif", "gif", "webp"
    );

    // Upload API
    @Override
    public UploadSdsResponseDto uploadSdsFile(UploadRequestDto request) {

        UploadSdsResponseDto response = new UploadSdsResponseDto();

        try {

            MultipartFile file = request.getFile();

            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            validateFileType(file);

            String originalName = file.getOriginalFilename();

            if (originalName == null) {
                throw new RuntimeException("Invalid file name");
            }

            originalName = originalName
                    .replace("\"", "")
                    .replace("'", "")
                    .replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

            // OCR
            String rawText = runOCR(file);

            String cleanedText = cleanOcrText(rawText);

            double confidenceScore = 0.90;

            // Detect Section
            String sectionType = detectSectionType(cleanedText);

            if ("UNKNOWN".equals(sectionType)) {
                response.setStatus("OCR_FAILED");
                response.setMessage("Unable to detect section type");
                return response;
            }

            String extractedProduct = extractProductIdentifier(cleanedText);

            String productIdentifier = normalizeText(extractedProduct);

            String sdsNumber = extractSdsNumber(cleanedText);

            if (sdsNumber == null
                    || sdsNumber.isBlank()
                    || sdsNumber.equalsIgnoreCase("Number")) {

                sdsNumber = "SDS_" + System.currentTimeMillis();
            }

            String manufacturerInfo = extractManufacturerInfo(cleanedText);

            String otherIdentification = extractOtherIdentification(cleanedText);

            String recommendedUse = extractRecommendedUse(cleanedText);

            String recommendedRestrictions = extractRecommendedRestrictions(cleanedText);

            if ((productIdentifier == null
                    || productIdentifier.isBlank())
                    && !"SECTION_2".equals(sectionType)) {

                response.setStatus("OCR_FAILED");
                response.setMessage("Product Identifier not detected.");

                return response;
            }

            if (productIdentifier == null || productIdentifier.isBlank()) {
                productIdentifier = "UNKNOWN_PRODUCT";
            }

            // Duplicate Check
            UploadProjection duplicate =
                    uploadRepository.findDuplicateSds(
                            productIdentifier,
                            sdsNumber
                    );

            if (duplicate != null) {

                uploadRepository.logDuplicate(
                        productIdentifier,
                        duplicate.getSdsId(),
                        "UPLOAD_BLOCKED"
                );

                response.setStatus("DUPLICATE");
                response.setSdsId(duplicate.getSdsId());
                response.setMessage("Already exists. Duplicate upload not allowed.");

                return response;
            }

            // Insert Master
            uploadRepository.insertSdsMaster(
                    productIdentifier,
                    sdsNumber,
                    manufacturerInfo,
                    1L
            );

            Long sdsId =
                    uploadRepository.getSdsId(
                            productIdentifier,
                            sdsNumber
                    );

            // Insert Version
            uploadRepository.insertVersion(
                    sdsId,
                    1,
                    originalName,
                    file.getContentType(),
                    file.getBytes(),
                    "Initial Upload",
                    1L
            );

            Long versionId = uploadRepository.getVersionId(sdsId);

            // Section Handling
            try {

                switch (sectionType) {

                    case "SECTION_1":

                        uploadRepository.insertSection1(
                                sdsId,
                                versionId,
                                productIdentifier,
                                otherIdentification,
                                sdsNumber,
                                recommendedUse,
                                recommendedRestrictions,
                                manufacturerInfo,
                                "OCR"
                        );

                        break;

                    case "SECTION_2":

                        cleanedText = cleanedText.length() > 2000
                                ? cleanedText.substring(0, 2000)
                                : cleanedText;

                        handleSection2(
                                cleanedText,
                                sdsId,
                                versionId
                        );

                        break;
                }

            } catch (Exception ex) {

                log.error("Section processing failed", ex);

                response.setStatus("SECTION_FAILED");
                response.setMessage(
                        "Section processing failed: "
                                + ex.getMessage()
                );

                response.setSdsId(sdsId);

                return response;
            }

            // OCR Result Save
            uploadRepository.saveOcrResult(
                    sdsId,
                    versionId,
                    rawText,
                    confidenceScore
            );

            uploadRepository.insertAuditLog(
                    sdsId,
                    "UPLOAD",
                    1L,
                    "New SDS uploaded via OCR"
            );

            response.setStatus("SUCCESS");
            response.setSdsId(sdsId);
            response.setVersion(1);
            response.setMessage("SDS uploaded successfully");

            return response;

        } catch (Exception e) {

            log.error("Upload failed", e);

            throw new RuntimeException(
                    "Upload failed: " + e.getMessage()
            );
        }
    }

    // ==========================================================
    // HELPER METHOD 1 & 2
    // ==========================================================
    // Validate File Type
    private void validateFileType(MultipartFile file) {

        String name = file.getOriginalFilename();

        if (name == null) {
            throw new RuntimeException("Invalid file name");
        }

        if (!name.contains(".")) {
            throw new RuntimeException("File extension missing");
        }

        String extension =
                name.substring(name.lastIndexOf(".") + 1)
                        .toLowerCase();

        if (!ALLOWED_TYPES.contains(extension)) {
            throw new RuntimeException("Unsupported file type");
        }
    }

    // Run OCR
    private String runOCR(MultipartFile file) throws Exception {

        String name = file.getOriginalFilename();

        String extension =
                name.substring(name.lastIndexOf(".") + 1)
                        .toLowerCase();

        if (extension.equals("pdf")) {
            return readPdf(file);
        }

        if (extension.equals("txt")) {
            return new String(file.getBytes());
        }

        if (extension.equals("doc")
                || extension.equals("docx")) {

            return readWord(file);
        }

        return readImage(file);
    }

    // Image OCR
    private String readImage(MultipartFile file) throws Exception {

        BufferedImage image =
                ImageIO.read(file.getInputStream());

        if (image == null) {
            throw new RuntimeException("Unable to read image file");
        }

        ITesseract tesseract = new Tesseract();

        tesseract.setDatapath(tessDataPath);

        tesseract.setLanguage("eng");

        return tesseract.doOCR(image);
    }

    // PDF OCR
    private String readPdf(MultipartFile file) throws Exception {

        StringBuilder text = new StringBuilder();

        try (PDDocument document =
                     PDDocument.load(file.getInputStream())) {

            PDFRenderer renderer =
                    new PDFRenderer(document);

            ITesseract tesseract =
                    new Tesseract();

            tesseract.setDatapath(tessDataPath);

            tesseract.setLanguage("eng");

            for (int i = 0;
                 i < document.getNumberOfPages();
                 i++) {

                BufferedImage image =
                        renderer.renderImageWithDPI(i, 150);

                if (image != null) {
                    text.append(
                            tesseract.doOCR(image)
                    ).append("\n");
                }
            }
        }

        return text.toString();
    }

    // Word Reader
    private String readWord(MultipartFile file) throws Exception {

        StringBuilder text = new StringBuilder();

        try (XWPFDocument doc =
                     new XWPFDocument(
                             file.getInputStream())) {

            doc.getParagraphs()
                    .forEach(p ->
                            text.append(p.getText())
                                    .append("\n"));
        }

        return text.toString();
    }

    // Clean OCR Text
    private String cleanOcrText(String text) {

        if (text == null) return "";

        return text.replaceAll("[^\\x00-\\x7F]", " ")
                .replaceAll("[*]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    // Normalize
    private String normalizeText(String text) {

        if (text == null) return null;

        return text.toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    // Product Identifier
    private String extractProductIdentifier(String text) {

        if (text == null || text.isBlank()) {
            return null;
        }

        Pattern pattern = Pattern.compile(
                "(?:Product Identifier|Product Name|Chemical Name|Substance Name)\\s*[:\\-]?\\s*([A-Za-z0-9()\\-, ]{2,150})",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {

            String value = matcher.group(1);

            value = value.split("Other Name")[0];
            value = value.split("SDS Number")[0];
            value = value.split("Recommended Use")[0];
            value = value.split("Manufacturer")[0];
            value = value.split("Revision")[0];
            value = value.split("Date")[0];

            return value.trim();
        }

        return null;
    }

    // SDS Number
    private String extractSdsNumber(String text) {

        if (text == null || text.isBlank()) {
            return null;
        }

        Pattern pattern = Pattern.compile(
                "(?:SDS\\s*(?:No|Number)?|Document\\s*No)\\s*[:\\-]?\\s*([A-Za-z0-9\\-]{3,50})",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {

            String value = matcher.group(1);

            value = value.split("Recommended")[0];
            value = value.split("Manufacturer")[0];

            return value.trim();
        }

        return null;
    }

    // Manufacturer Info
    private String extractManufacturerInfo(String text) {

        if (text == null || text.isBlank()) {
            return null;
        }

        Pattern pattern = Pattern.compile(
                "(Manufacturer|Company|Supplier|Manufactured By)\\s*[:\\-]?\\s*([A-Za-z0-9 ,.&()\\-]{5,200})",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {

            String value = matcher.group(2);

            value = value.split("Section")[0];
            value = value.split("Signal Word")[0];
            value = value.split("Hazard")[0];

            return value.trim();
        }

        return null;
    }

    // Other Identification
    private String extractOtherIdentification(String text) {

        if (text == null) return null;

        Pattern pattern = Pattern.compile(
                "(Other Name|Synonym|Other Identification)\\s*[:\\-]?\\s*([^\\n]{2,100})",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? matcher.group(2).trim() : null;
    }

    // Recommended Use
    private String extractRecommendedUse(String text) {

        if (text == null) return null;

        Pattern pattern = Pattern.compile(
                "(Recommended Use|Use of Substance|Intended Use)\\s*[:\\-]?\\s*([^\\n]{2,200})",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? matcher.group(2).trim() : null;
    }

    // Recommended Restrictions
    private String extractRecommendedRestrictions(String text) {

        if (text == null) return null;

        Pattern pattern = Pattern.compile(
                "(Restrictions on Use|Recommended Restrictions)\\s*[:\\-]?\\s*([^\\n]{2,200})",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? matcher.group(2).trim() : null;
    }

    // Detect Section
    private String detectSectionType(String text) {

        if (text == null) {
            return "UNKNOWN";
        }

        text = text.toLowerCase();

        if (text.contains("section 2")
                || text.contains("hazard identification")) {
            return "SECTION_2";
        }
        if (text.contains("section 1")
                || text.contains("identification")) {

            return "SECTION_1";
        }
        return "UNKNOWN";
    }

    // Section 2
    private void handleSection2(
            String text,
            Long sdsId,
            Long versionId) {

        uploadRepository.insertSection2(
                sdsId,
                versionId,
                "Not Available",
                "WARNING",
                null,
                "OCR"
        );
    }




    // ================= UPLOAD SECTION 5 AND 6 =================
    @Override
    @Transactional
    public UploadSdsResponseDto uploadSdsSection5And6(UploadRequestDto uploadRequest) {
        UploadSdsResponseDto uploadResponse = new UploadSdsResponseDto();
        try {
            // ================= FILE =================
            MultipartFile uploadedFile = uploadRequest.getFile();
            if (uploadedFile == null || uploadedFile.isEmpty()) {
                throw new RuntimeException("File is empty");
            }
            String uploadedFileName = uploadedFile.getOriginalFilename();
            String uploadedFileType = uploadedFile.getContentType();
            byte[] uploadedFileData = uploadedFile.getBytes();

            // ================= FILE HASH =================
            String uploadedFileHash = generateHash(uploadedFileData);

            // ================= DUPLICATE CHECK =================
            if (repository.checkDuplicateFile(uploadedFileHash) > 0) {
                throw new RuntimeException("Duplicate file already uploaded");
            }

            // ================= SDS ID =================
            Long generatedSdsId = System.currentTimeMillis();

            // ================= OCR =================
            String extractedOcrText = cleanOcrText(runOCR(uploadedFile));

            System.out.println("=========== OCR TEXT ===========");
            System.out.println(extractedOcrText);
            System.out.println("================================");

            // ================= SECTION TYPE =================
            String requestedSectionType = uploadRequest.getSectionType();

            if (requestedSectionType == null || requestedSectionType.isBlank()) {
                requestedSectionType = detectSectionTypeFor5And6(
                        extractedOcrText
                );
            }

            System.out.println("DETECTED SECTION TYPE : "
                    + requestedSectionType);

            // ================= UNKNOWN CHECK =================
            if ("UNKNOWN".equalsIgnoreCase(requestedSectionType)) {
                uploadResponse.setStatus("OCR_FAILED");
                uploadResponse.setMessage(
                        "Unable to detect section type"
                );
                return uploadResponse;
            }

            // ================= SWITCH =================
            switch (requestedSectionType.trim().toUpperCase()) {
                // SECTION 5
                case "SECTION_5":
                    repository.insertSection5Data(
                            generatedSdsId,
                            // Suitable Media
                            extractSectionBlock(
                                    extractedOcrText,
                                    "Suitable extinguishing media",
                                    "Unsuitable extinguishing media"
                            ),

                            // Unsuitable Media
                            extractSectionBlock(
                                    extractedOcrText,
                                    "Unsuitable extinguishing media",
                                    "Special hazards arising"
                            ),

                            // Special Hazards
                            extractSectionBlock(
                                    extractedOcrText,
                                    "Special hazards arising",
                                    "Advice for firefighters"
                            ),

                            // Advice
                            extractSectionBlock(
                                    extractedOcrText,
                                    "Advice for firefighters",
                                    "Protective equipment"
                            ),

                            // Protective Equipment
                            extractSectionBlock(
                                    extractedOcrText,
                                    "Protective equipment",
                                    null
                            ),

                            uploadedFileData,
                            uploadedFileName,
                            uploadedFileType,
                            uploadedFileHash
                    );
                    break;

                // SECTION 6
                case "SECTION_6":
                    repository.insertSection6Data(
                            generatedSdsId,
                            // Personal Precautions
                            extractSectionBlock(
                                    extractedOcrText,
                                    "Personal precautions",
                                    "Environmental precautions"
                            ),
                            // Environmental Precautions
                            extractSectionBlock(
                                    extractedOcrText,
                                    "Environmental precautions",
                                    "Methods and material for containment and cleaning up"
                            ),
                            // Containment Methods
                            extractSectionBlock(
                                    extractedOcrText,
                                    "Methods and material for containment and cleaning up",
                                    "Reference to other sections"
                            ),
                            // Reference Sections
                            extractSectionBlock(
                                    extractedOcrText,
                                    "Reference to other sections",
                                    null
                            ),
                            uploadedFileData,
                            uploadedFileName,
                            uploadedFileType,
                            uploadedFileHash
                    );
                    break;

                // INVALID
                default:
                    throw new RuntimeException(
                            "Invalid Section Type"
                    );
            }

            // ================= SUCCESS RESPONSE =================
            uploadResponse.setStatus("SUCCESS");
            uploadResponse.setSdsId(generatedSdsId);
            uploadResponse.setVersion(1);
            uploadResponse.setMessage("Uploaded successfully");
            return uploadResponse;
        } catch (Exception exception) {
            exception.printStackTrace();
            uploadResponse.setStatus("FAILED");
            uploadResponse.setMessage(
                    "Upload failed : " + exception.getMessage()
            );
            return uploadResponse;
        }
    }


    // ==========================================================
    // HELPER METHOD : DETECT SECTION TYPE  5 & 6
    // ==========================================================
        private String detectSectionTypeFor5And6(String text) {
            try {
                if (text == null || text.isBlank()) {
                    return "UNKNOWN";
                }
                text = text.toLowerCase();

                // ==================================================
                // SECTION 5
                // ==================================================
                if (text.contains("section 5")
                        || text.contains("fire-fighting measures")
                        || text.contains("fire fighting measures")
                        || text.contains("suitable extinguishing media")
                        || text.contains("advice for firefighters")) {

                    return "SECTION_5";
                }

                // ==================================================
                // SECTION 6
                // ==================================================
                if (text.contains("section 6")
                        || text.contains("accidental release measures")
                        || text.contains("personal precautions")
                        || text.contains("environmental precautions")
                        || text.contains("containment methods")) {

                    return "SECTION_6";
                }
                return "UNKNOWN";
            } catch (Exception exception) {
                exception.printStackTrace();
                return "UNKNOWN";
            }
        }

    // HELPER METHOD : EXTRACT BLOCK 5 & 6
    public static String extractSectionBlock(
            String completeText,
            String startKeyword,
            String endKeyword
    ) {
        try {
            if (completeText == null || completeText.isBlank()) {
                return null;
            }

            // ================= NORMALIZE TEXT =================
            String normalizedText = completeText
                    .replace("\n", " ")
                    .replace("\r", " ")
                    .replaceAll("\\s+", " ")
                    .trim();

            String lowerCaseText = normalizedText.toLowerCase();
            String start = startKeyword.toLowerCase().trim();

            // ================= START INDEX =================
            int startIndex = lowerCaseText.indexOf(start);
            if (startIndex == -1) {
                System.out.println("START KEYWORD NOT FOUND : " + startKeyword);
                return null;
            }

            // ================= MOVE AFTER START KEYWORD =================
            startIndex = startIndex + start.length();

            // ================= END INDEX =================
            int endIndex;
            if (endKeyword != null) {
                String end = endKeyword.toLowerCase().trim();
                endIndex = lowerCaseText.indexOf(end, startIndex);
                if (endIndex == -1) {
                    endIndex = normalizedText.length();
                }
            } else {
                endIndex =
                        normalizedText.length();
            }

            // ================= EXTRACTED VALUE =================
            String extractedText =
                    normalizedText.substring(
                            startIndex,
                            endIndex
                    ).trim();
            System.out.println(
                    "EXTRACTED BLOCK : "
                            + extractedText
            );
            return extractedText;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }



    // ================= SECTION 9 10 11 UPLOAD =================
    @Override
    @Transactional
    public UploadSdsResponseDto uploadSection9And10And11(UploadRequestDto request) {
        UploadSdsResponseDto response = new UploadSdsResponseDto();

        MultipartFile file = request.getFile();

        if (file == null || file.isEmpty()) {
            response.setStatus("FAILED");
            response.setMessage("File is empty");

            return response;
        }

        try {
            // ================= VALIDATE FILE =================
            validateFileTypeFor9And10And11(file);
            String fileName = file.getOriginalFilename();
            String fileType = file.getContentType();
            byte[] fileData = file.getBytes();

            // ================= HASH =================
            String fileHash = generateHashFor9And10And11(fileData);

            // ================= DUPLICATE CHECK =================
            Integer duplicateCount = upload9_10_11Repository.checkDuplicateFile(fileHash);

            if (duplicateCount > 0) {
                response.setStatus("DUPLICATE");
                response.setMessage("Duplicate file already uploaded");
                return response;
            }

            // ================= OCR =================
            String extractedText = cleanOcrTextFor9And10And11(runOCRFor9And10And11(file));

            System.out.println("=========== OCR TEXT ===========");
            System.out.println(extractedText);
            System.out.println("================================");

            // ================= DETECT SECTION =================
            String sectionType = request.getSectionType();

            if (sectionType == null || sectionType.isBlank()) {
                sectionType = detectSectionTypeFor9And10And11(extractedText);
            }

            System.out.println("DETECTED SECTION : " + sectionType);

            if ("UNKNOWN".equalsIgnoreCase(sectionType)) {
                response.setStatus("OCR_FAILED");
                response.setMessage("Unable to detect section type");
                return response;
            }

            // ================= SDS ID =================
            Long sdsId = System.currentTimeMillis();

            // SECTION SWITCH
            switch (sectionType.toUpperCase()) {
                // SECTION 9
                case "SECTION_9":
                    upload9_10_11Repository
                            .insertSection9Data(
                                    sdsId,
                                    extractValueFor9And10And11(extractedText, "Physical state"),
                                    extractValueFor9And10And11(extractedText, "Colour"),
                                    extractValueFor9And10And11(extractedText, "Odour"),
                                    extractValueFor9And10And11(extractedText, "Melting point"),
                                    extractValueFor9And10And11(extractedText, "Boiling point"),
                                    extractValueFor9And10And11(extractedText, "Flammability"),
                                    extractValueFor9And10And11(extractedText, "Explosion limit"),
                                    extractValueFor9And10And11(extractedText, "Flash point"),
                                    extractValueFor9And10And11(extractedText, "Auto-ignition temperature"),
                                    extractValueFor9And10And11(extractedText, "Decomposition temperature"),
                                    extractValueFor9And10And11(extractedText, "pH"),
                                    extractValueFor9And10And11(extractedText, "Kinematic viscosity"),
                                    extractValueFor9And10And11(extractedText, "Solubility"),
                                    extractValueFor9And10And11(extractedText, "Partition coefficient"),
                                    extractValueFor9And10And11(extractedText, "Vapour pressure"),
                                    extractValueFor9And10And11(extractedText, "Density"),
                                    extractValueFor9And10And11(extractedText, "Relative vapour density"),
                                    extractValueFor9And10And11(extractedText, "Particle characteristics"),
                                    extractValueFor9And10And11(extractedText, "Particle size"),
                                    extractValueFor9And10And11(extractedText, "Explosive properties"),
                                    extractValueFor9And10And11(extractedText, "Oxidising properties"),
                                    extractValueFor9And10And11(extractedText, "Evaporation rate"),
                                    extractValueFor9And10And11(extractedText, "Viscosity"),
                                    extractValueFor9And10And11(extractedText, "Bulk density"),
                                    extractValueFor9And10And11(extractedText, "Moisture"),
                                    extractValueFor9And10And11(extractedText, "VOC content"),
                                    extractValueFor9And10And11(extractedText, "Other information"),
                                    fileData,
                                    fileName,
                                    fileType,
                                    fileHash
                            );
                    break;

                // SECTION 10
                case "SECTION_10":
                    insertSection10Item(
                            sdsId,
                            "10.1 Reactivity",
                            extractSectionBlock9And10And11(extractedText, "10.1", "10.2"),
                            fileData,
                            fileName,
                            fileType,
                            fileHash
                    );

                    insertSection10Item(
                            sdsId,
                            "10.2 Chemical stability",
                            extractSectionBlock9And10And11(extractedText, "10.2", "10.3"),
                            fileData,
                            fileName,
                            fileType,
                            fileHash
                    );
                    break;

                // SECTION 11
                case "SECTION_11":
                    upload9_10_11Repository
                            .insertSection11Data(
                                    sdsId,
                                    extractSectionBlock9And10And11(extractedText, "11.1", "11.1.1"),
                                    extractSectionBlock9And10And11(extractedText, "11.1.1", "11.1.2"),
                                    extractSectionBlock9And10And11(extractedText, "11.1.2", "11.1.3"),
                                    extractSectionBlock9And10And11(extractedText, "11.1.3", "11.1.4"),
                                    extractSectionBlock9And10And11(extractedText, "11.2", null),
                                    fileData,
                                    fileName,
                                    fileType,
                                    fileHash
                            );
                    break;

                default:
                    response.setStatus("OCR_FAILED");
                    response.setMessage("Unable to detect section type");
                    return response;
            }

            // ================= SUCCESS =================
            response.setStatus("SUCCESS");
            response.setSdsId(sdsId);
            response.setVersion(1);
            response.setMessage("Uploaded successfully");
            return response;

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }

// ================= SECTION 10 INSERT =================
    private void insertSection10Item(
            Long sdsId,
            String itemName,
            String information,
            byte[] fileData,
            String fileName,
            String fileType,
            String fileHash
    ) {
        try {
            upload9_10_11Repository
                    .insertSection10Data(
                            sdsId,
                            itemName,
                            information,
                            "",
                            false,
                            fileData,
                            fileName,
                            fileType,
                            fileHash
                    );
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Failed to insert section 10 item");
        }
    }


// ================= DETECT SECTION TYPE =================
    private String detectSectionTypeFor9And10And11(
            String text
    ) {
        try {
            if (text == null || text.isBlank()) {
                return "UNKNOWN";
            }
            text = text.toLowerCase();

            if (text.contains("section 9")
                    || text.contains("physical and chemical properties")
                    || text.contains("physical state")
                    || text.contains("vapour pressure")
                    || text.contains("flash point")) {

                return "SECTION_9";
            }

            if (text.contains("section 10")
                    || text.contains("stability and reactivity")
                    || text.contains("chemical stability")
                    || text.contains("hazardous reactions")
                    || text.contains("incompatible materials")) {

                return "SECTION_10";
            }

            if (text.contains("section 11")
                    || text.contains("toxicological information")
                    || text.contains("routes of exposure")
                    || text.contains("symptoms related")
                    || text.contains("delayed and immediate effects")) {

                return "SECTION_11";
            }
            return "UNKNOWN";
        } catch (Exception exception) {
            exception.printStackTrace();
            return "UNKNOWN";
        }
    }

// ================= EXTRACT SECTION BLOCK =================
public static String extractSectionBlock9And10And11(
        String completeText,
        String startKeyword,
        String endKeyword
) {
    try {
        if (completeText == null || completeText.isBlank()) {
            return null;
        }

        String normalizedText =
                completeText
                        .replace("\n", " ")
                        .replace("\r", " ")
                        .replaceAll("\\s+", " ")
                        .trim();

        String lowerCaseText = normalizedText.toLowerCase();

        String start = startKeyword.toLowerCase().trim();

        String end = endKeyword != null ? endKeyword.toLowerCase().trim() : null;

        int startIndex = lowerCaseText.indexOf(start);

        if (startIndex == -1) {
            return null;
        }

        int contentStart = startIndex + start.length();
        int endIndex;
        if (end != null) {

            endIndex = lowerCaseText.indexOf(end, contentStart);

            if (endIndex == -1) {
                endIndex = normalizedText.length();
            }
        } else {
            endIndex = normalizedText.length();
        }

        String extractedText = normalizedText.substring(contentStart, endIndex).trim();

        if (extractedText.isBlank()) {
            return null;
        }

        return extractedText;

    } catch (Exception exception) {
        exception.printStackTrace();
        return null;
    }
}

// ================= EXTRACT VALUE =================
    private String extractValueFor9And10And11(String text, String label) {
        try {
            if (text == null || text.isBlank()) {
                return null;
            }
            Pattern pattern = Pattern.compile(label + "\\s*[:\\-]?\\s*(.{1,200})", Pattern.CASE_INSENSITIVE);

            Matcher matcher =pattern.matcher(text);

            if (matcher.find()) {
                String value = matcher.group(1);

                value = value.split("Section")[0];

                return value.trim();
            }
            return null;

        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

// ================= CLEAN OCR TEXT =================

    private String cleanOcrTextFor9And10And11(
            String text
    ) {
        try {
            if (text == null) {
                return "";
            }
            return text
                    .replaceAll("[^\\x00-\\x7F]", " ")
                    .replaceAll("[*]", "")
                    .replaceAll("\\s+", " ")
                    .trim();

        } catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }

// ================= VALIDATE FILE TYPE =================

    private void validateFileTypeFor9And10And11(
            MultipartFile file
    ) {
        try {
            String name = file.getOriginalFilename();

            if (name == null) {

                throw new RuntimeException(
                        "Invalid file name"
                );
            }

            String extension =
                    name.substring(
                                    name.lastIndexOf(".") + 1
                            )
                            .toLowerCase();

            Set<String> allowedTypes =
                    Set.of(
                            "pdf",
                            "doc",
                            "docx",
                            "txt",
                            "jpg",
                            "jpeg",
                            "png",
                            "bmp",
                            "tiff",
                            "tif",
                            "gif",
                            "webp"
                    );

            if (!allowedTypes.contains(extension)) {

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

// ================= RUN OCR =================

    private String runOCRFor9And10And11(MultipartFile file) throws Exception {
        String name = file.getOriginalFilename();
        String extension = name.substring(name.lastIndexOf(".") + 1).toLowerCase();

        if (extension.equals("pdf")) {
            return readPdfFor9And10And11(file);
        }

        if (extension.equals("txt")) {
            return new String(file.getBytes());
        }

        if (extension.equals("doc") || extension.equals("docx")) {
            return readWordFor9And10And11(file);
        }

        return readImageFor9And10And11(file);
    }

// ================= READ IMAGE =================

    private String readImageFor9And10And11(MultipartFile file) throws Exception {
        BufferedImage image = ImageIO.read(file.getInputStream());

        if (image == null) {
            throw new RuntimeException("Unable to read image file");
        }

        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessDataPath);
        tesseract.setLanguage("eng");
        return tesseract.doOCR(image);
    }

// ================= READ PDF =================
    private String readPdfFor9And10And11(MultipartFile file) throws Exception {
        StringBuilder text = new StringBuilder();
        try (PDDocument document = PDDocument.load(file.getInputStream())) {

            PDFRenderer renderer = new PDFRenderer(document);
            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath(tessDataPath);
            tesseract.setLanguage("eng");
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 150);
                if (image != null) {
                    text.append(tesseract.doOCR(image)).append("\n");
                }
            }
        }

        return text.toString();
    }

// ================= READ WORD =================
    private String readWordFor9And10And11(MultipartFile file) throws Exception {
        StringBuilder text = new StringBuilder();
        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
            document.getParagraphs().forEach(paragraph ->
                            text.append(
                                    paragraph.getText()
                                    )
                                    .append("\n")
                    );
        }

        return text.toString();
    }

// ================= GENERATE HASH =================
    public static String generateHashFor9And10And11(byte[] data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(data);
        StringBuilder builder =new StringBuilder();
        for (byte singleByte : hashBytes) {
            builder.append(
                    String.format(
                            "%02x",
                            singleByte
                    )
            );
        }
        return builder.toString();
    }
}