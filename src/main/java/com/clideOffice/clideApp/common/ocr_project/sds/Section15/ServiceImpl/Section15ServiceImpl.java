package com.clideOffice.clideApp.common.ocr_project.sds.Section15.ServiceImpl;

import com.clideOffice.clideApp.common.ocr_project.sds.Section15.Service.Section15Service;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section15.RegulatoryInformation;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.Section15Projection;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.Section15Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.Upload15And16Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section15RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section15ResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadSdsResponseDto;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.security.MessageDigest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class Section15ServiceImpl implements Section15Service {

    private final Section15Repository repository;
    private final Upload15And16Repository upload15And16Repository;

    @Value("${tesseract.datapath}")
    private String tessDataPath;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "pdf", "doc", "docx", "txt",
            "jpg", "jpeg", "png", "bmp",
            "tiff", "tif", "gif", "webp"
    );

    // ================= SERVICE METHOD =================
    @Override
    public UploadSdsResponseDto uploadSection15And16(
            UploadRequestDto request
    ) {

        UploadSdsResponseDto response =
                new UploadSdsResponseDto();

        try {

            MultipartFile file =
                    request.getFile();

            if (file == null || file.isEmpty()) {

                response.setStatus("FAILED");

                response.setMessage("File is empty");

                return response;
            }

            String fileName =
                    file.getOriginalFilename();

            String fileType =
                    file.getContentType();

            byte[] fileData =
                    file.getBytes();

            // ================= HASH =================

            String fileHash =
                    generateHashForSection15And16(
                            fileData
                    );

            // ================= DUPLICATE =================

            Integer duplicateCount =
                    upload15And16Repository
                            .checkDuplicateFile(
                                    fileHash
                            );

            if (duplicateCount != null
                    && duplicateCount > 0) {

                response.setStatus("DUPLICATE");

                response.setMessage(
                        "Duplicate file already uploaded"
                );

                return response;
            }

            // ================= OCR =================

            String extractedText =
                    cleanOcrTextForSection15And16(
                            runOCRForSection15And16(file)
                    );

            // ================= DETECT SECTION =================

            String sectionType =
                    request.getSectionType();

            if (sectionType == null
                    || sectionType.isBlank()) {

                sectionType =
                        detectSectionTypeFor15And16(
                                extractedText
                        );
            }

            if ("UNKNOWN".equalsIgnoreCase(
                    sectionType
            )) {

                response.setStatus("OCR_FAILED");

                response.setMessage(
                        "Unable to detect section type"
                );

                return response;
            }

            // ================= SDS ID =================

            Long sdsId =
                    upload15And16Repository
                            .generateNextSdsId();

            // ================= SWITCH =================

            switch (sectionType.toUpperCase()) {

                // ================= SECTION 15 =================

                case "SECTION_15":

                    String section15Text =
                            extractSection15Block(
                                    extractedText
                            );

                    upload15And16Repository
                            .insertSection15Data(

                                    sdsId,

                                    extractSection15SafetyHealthEnvironmentalRegulations(
                                            section15Text
                                    ),

                                    extractSection15Directive2012_18_EU(
                                            section15Text
                                    ),

                                    extractSection15ReachAnnexXVII(
                                            section15Text
                                    ),

                                    extractSection15Directive2011_65_EU(
                                            section15Text
                                    ),

                                    extractSection15RegulationEU2019_1148(
                                            section15Text
                                    ),

                                    extractSection15RegulationEC273_2004(
                                            section15Text
                                    ),

                                    extractSection15RegulationEC111_2005(
                                            section15Text
                                    ),

                                    extractSection15ChemicalSafetyAssessment(
                                            section15Text
                                    ),

                                    fileData,

                                    fileName,

                                    fileType,

                                    fileHash
                            );

                    break;

                // ================= SECTION 16 =================

                case "SECTION_16":

                    String section16Text =
                            extractSection16Block(
                                    extractedText
                            );

                    upload15And16Repository
                            .insertSection16Data(

                                    sdsId,

                                    extractSection16IndicationOfChanges(
                                            section16Text
                                    ),

                                    extractSection16Abbreviations(
                                            section16Text
                                    ),

                                    extractSection16References(
                                            section16Text
                                    ),

                                    extractSection16ClassificationProcedure(
                                            section16Text
                                    ),

                                    extractSection16HPhrases(
                                            section16Text
                                    ),

                                    extractSection16TrainingAdvice(
                                            section16Text
                                    ),

                                    extractSection16AdditionalInformation(
                                            section16Text
                                    ),

                                    extractSection16DateOfIssue(
                                            section16Text
                                    ),

                                    extractSection16DateOfRevision(
                                            section16Text
                                    ),

                                    extractSection16Version(
                                            section16Text
                                    ),

                                    extractSection16PreparedBy(
                                            section16Text
                                    ),

                                    extractSection16SdsNumber(
                                            section16Text
                                    ),

                                    extractSection16Disclaimer(
                                            section16Text
                                    ),

                                    fileData,

                                    fileName,

                                    fileType,

                                    fileHash
                            );

                    break;

                default:

                    response.setStatus("FAILED");

                    response.setMessage(
                            "Invalid section type"
                    );

                    return response;
            }

            // ================= SUCCESS =================

            response.setStatus("SUCCESS");

            response.setSdsId(sdsId);

            response.setVersion(1);

            response.setMessage(
                    "Uploaded successfully"
            );

            return response;

        } catch (Exception exception) {

            exception.printStackTrace();

            response.setStatus("FAILED");

            response.setMessage(
                    exception.getMessage()
            );

            return response;
        }
    }


// ================= DETECT SECTION TYPE =================

    private String detectSectionTypeFor15And16(
            String text
    ) {

        try {

            if (text == null
                    || text.isBlank()) {

                return "UNKNOWN";
            }

            text = text.toLowerCase();

            if (text.contains("section 15")
                    || text.contains("regulatory information")
                    || text.contains("chemical safety assessment")) {

                return "SECTION_15";
            }

            if (text.contains("section 16")
                    || text.contains("other information")
                    || text.contains("date of revision")
                    || text.contains("prepared by")) {

                return "SECTION_16";
            }

            return "UNKNOWN";

        } catch (Exception exception) {

            exception.printStackTrace();

            return "UNKNOWN";
        }
    }


// ================= OCR METHOD =================

    private String runOCRForSection15And16(
            MultipartFile file
    ) throws Exception {

        String fileName =
                file.getOriginalFilename();

        if (fileName == null) {

            throw new RuntimeException(
                    "Invalid file name"
            );
        }

        String extension =
                fileName.substring(
                        fileName.lastIndexOf(".") + 1
                ).toLowerCase();

        if (extension.equals("pdf")) {

            return readPdfForSection15And16(file);
        }

        if (extension.equals("txt")) {

            return new String(file.getBytes());
        }

        if (extension.equals("doc")
                || extension.equals("docx")) {

            return readWordForSection15And16(file);
        }

        return readImageForSection15And16(file);
    }


// ================= CLEAN OCR TEXT =================

    private String cleanOcrTextForSection15And16(
            String text
    ) {

        try {

            if (text == null) {
                return "";
            }

            return text
                    .replaceAll("[^\\x00-\\x7F]", " ")
                    .replaceAll("\\s+", " ")
                    .trim();

        } catch (Exception exception) {

            exception.printStackTrace();

            return "";
        }
    }


// ================= GENERATE HASH =================

    private String generateHashForSection15And16(
            byte[] fileData
    ) throws Exception {

        MessageDigest digest =
                MessageDigest.getInstance(
                        "SHA-256"
                );

        byte[] hashBytes =
                digest.digest(fileData);

        StringBuilder builder =
                new StringBuilder();

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


// ================= READ IMAGE =================

    private String readImageForSection15And16(
            MultipartFile file
    ) throws Exception {

        BufferedImage image =
                ImageIO.read(
                        file.getInputStream()
                );

        if (image == null) {

            throw new RuntimeException(
                    "Unable to read image"
            );
        }

        ITesseract tesseract =
                new Tesseract();

        tesseract.setDatapath(
                tessDataPath
        );

        tesseract.setLanguage("eng");

        return tesseract.doOCR(image);
    }


// ================= READ PDF =================

    private String readPdfForSection15And16(
            MultipartFile file
    ) throws Exception {

        StringBuilder text =
                new StringBuilder();

        try (PDDocument document =
                     PDDocument.load(
                             file.getInputStream()
                     )) {

            PDFRenderer renderer =
                    new PDFRenderer(document);

            ITesseract tesseract =
                    new Tesseract();

            tesseract.setDatapath(
                    tessDataPath
            );

            tesseract.setLanguage("eng");

            for (int i = 0;
                 i < document.getNumberOfPages();
                 i++) {

                BufferedImage image =
                        renderer.renderImageWithDPI(
                                i,
                                150
                        );

                text.append(
                        tesseract.doOCR(image)
                ).append("\n");
            }
        }

        return text.toString();
    }


// ================= READ WORD =================

    private String readWordForSection15And16(
            MultipartFile file
    ) throws Exception {

        StringBuilder text =
                new StringBuilder();

        try (XWPFDocument document =
                     new XWPFDocument(
                             file.getInputStream()
                     )) {

            document.getParagraphs()
                    .forEach(paragraph ->

                            text.append(
                                    paragraph.getText()
                            ).append("\n")
                    );
        }

        return text.toString();
    }


// ================= SECTION BLOCK =================

    private String extractSection15Block(
            String completeText
    ) {

        return extractSection15And16Block(
                completeText,
                "SECTION 15",
                "SECTION 16"
        );
    }

    private String extractSection16Block(
            String completeText
    ) {

        return extractSection15And16Block(
                completeText,
                "SECTION 16",
                null
        );
    }

    private String extractSection15And16Block(
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

            String lowerText =
                    normalizedText.toLowerCase();

            int startIndex =
                    lowerText.indexOf(
                            startKeyword.toLowerCase()
                    );

            if (startIndex == -1) {
                return null;
            }

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

            return normalizedText
                    .substring(startIndex, endIndex)
                    .trim();

        } catch (Exception exception) {

            exception.printStackTrace();

            return null;
        }
    }


// ================= COMMON FIELD EXTRACTION =================

    private String extractSection15And16Field(
            String text,
            String startKeyword,
            String endKeyword
    ) {

        try {

            if (text == null
                    || text.isBlank()) {

                return null;
            }

            String normalizedText =
                    text
                            .replace("\n", " ")
                            .replace("\r", " ")
                            .replaceAll("\\s+", " ")
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
                    startIndex + startKeyword.length();

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

            return normalizedText
                    .substring(startIndex, endIndex)
                    .replace(":", "")
                    .trim();

        } catch (Exception exception) {

            exception.printStackTrace();

            return null;
        }
    }


// ================= SECTION 15 =================

    private String extractSection15SafetyHealthEnvironmentalRegulations(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "15.1",
                "15.2"
        );
    }

    private String extractSection15Directive2012_18_EU(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "15.2",
                "15.3"
        );
    }

    private String extractSection15ReachAnnexXVII(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "15.3",
                "15.4"
        );
    }

    private String extractSection15Directive2011_65_EU(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "15.4",
                "15.5"
        );
    }

    private String extractSection15RegulationEU2019_1148(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "15.5",
                "15.6"
        );
    }

    private String extractSection15RegulationEC273_2004(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "15.6",
                "15.7"
        );
    }

    private String extractSection15RegulationEC111_2005(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "15.7",
                "Chemical safety assessment"
        );
    }

    private String extractSection15ChemicalSafetyAssessment(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "Chemical safety assessment",
                null
        );
    }


// ================= SECTION 16 =================

    private String extractSection16IndicationOfChanges(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "16.1",
                "16.2"
        );
    }

    private String extractSection16Abbreviations(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "16.2",
                "16.3"
        );
    }

    private String extractSection16References(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "16.3",
                "16.4"
        );
    }

    private String extractSection16ClassificationProcedure(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "16.4",
                "16.5"
        );
    }

    private String extractSection16HPhrases(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "16.5",
                "16.6"
        );
    }

    private String extractSection16TrainingAdvice(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "16.6",
                "16.7"
        );
    }

    private String extractSection16AdditionalInformation(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "16.7",
                "Document Information"
        );
    }

    private String extractSection16DateOfIssue(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "Date of Issue",
                "Date of Revision"
        );
    }

    private String extractSection16DateOfRevision(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "Date of Revision",
                "Version"
        );
    }

    private String extractSection16Version(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "Version",
                "Prepared By"
        );
    }

    private String extractSection16PreparedBy(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "Prepared By",
                "SDS No"
        );
    }

    private String extractSection16SdsNumber(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "SDS No",
                "Disclaimer"
        );
    }

    private String extractSection16Disclaimer(
            String text
    ) {

        return extractSection15And16Field(
                text,
                "Disclaimer",
                null
        );
    }

    /* ================= UPSERT ================= */
    @Override
    @Transactional
    public Section15ResponseDTO addItem(Section15RequestDTO dto) {

        RegulatoryInformation entity = repository.findEntityBySdsId(dto.getSdsId())
                .orElse(RegulatoryInformation.builder()
                        .sdsId(dto.getSdsId())
                        .build());

        // 🔹 Set all fields
        entity.setSafetyHealthEnvironmentalRegulations(dto.getSafetyHealthEnvironmentalRegulations());
        entity.setDirective2012_18_EU(dto.getDirective2012_18_EU());
        entity.setReachAnnexXVII(dto.getReachAnnexXVII());
        entity.setDirective2011_65_EU(dto.getDirective2011_65_EU());
        entity.setRegulationEU2019_1148(dto.getRegulationEU2019_1148());
        entity.setRegulationEC273_2004(dto.getRegulationEC273_2004());
        entity.setRegulationEC111_2005(dto.getRegulationEC111_2005());
        entity.setChemicalSafetyAssessment(dto.getChemicalSafetyAssessment());

        RegulatoryInformation saved = repository.save(entity);

        return mapToResponse(saved);
    }

    /* ================= GET ================= */
    @Override
    public Section15ResponseDTO getBySdsId(Long sdsId) {

        Section15Projection p = repository.findBySdsId(sdsId)
                .orElseThrow(() -> new RuntimeException("Section15 data not found for sdsId: " + sdsId));

        return Section15ResponseDTO.builder()
                .id(p.getId())
                .sdsId(p.getSdsId())
                .safetyHealthEnvironmentalRegulations(p.getSafetyHealthEnvironmentalRegulations())
                .directive2012_18_EU(p.getDirective2012_18_EU())
                .reachAnnexXVII(p.getReachAnnexXVII())
                .directive2011_65_EU(p.getDirective2011_65_EU())
                .regulationEU2019_1148(p.getRegulationEU2019_1148())
                .regulationEC273_2004(p.getRegulationEC273_2004())
                .regulationEC111_2005(p.getRegulationEC111_2005())
                .chemicalSafetyAssessment(p.getChemicalSafetyAssessment())
                .build();
    }

    /* ================= DELETE ================= */
    @Override
    @Transactional
    public void deleteBySdsId(Long sdsId) {

        // 🔹 Optional check (recommended)
        repository.findBySdsId(sdsId)
                .orElseThrow(() -> new RuntimeException("Section15 not found for deletion"));

        repository.deleteBySdsId(sdsId);
    }

    /* ================= MAPPER ================= */
    private Section15ResponseDTO mapToResponse(RegulatoryInformation e) {

        return Section15ResponseDTO.builder()
                .id(e.getId())
                .sdsId(e.getSdsId())
                .safetyHealthEnvironmentalRegulations(e.getSafetyHealthEnvironmentalRegulations())
                .directive2012_18_EU(e.getDirective2012_18_EU())
                .reachAnnexXVII(e.getReachAnnexXVII())
                .directive2011_65_EU(e.getDirective2011_65_EU())
                .regulationEU2019_1148(e.getRegulationEU2019_1148())
                .regulationEC273_2004(e.getRegulationEC273_2004())
                .regulationEC111_2005(e.getRegulationEC111_2005())
                .chemicalSafetyAssessment(e.getChemicalSafetyAssessment())
                .build();
    }
}