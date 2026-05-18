package com.clideOffice.clideApp.common.ocr_project.sds.Section3.serviceImpl;

//import com.amazonaws.services.s3.AmazonS3;
import com.clideOffice.clideApp.common.ocr_project.sds.Section3.service.Section3Service;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section3.Ingredient;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section3.SpecialLimit;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.IngredientRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.SpecialLimitRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.UploadRepository3and4;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SpecialLimitRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UpdateIngredientRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.IngredientResponseDTO;

import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SpecialLimitResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Upload3and4ResponseDto;
import lombok.RequiredArgsConstructor;

import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Section3ServiceImpl implements Section3Service {

    private final IngredientRepository ingredientRepository;
    private final SpecialLimitRepository specialLimitRepository;
    private final UploadRepository3and4 repository;


//    private final AmazonS3 amazonS3;
//
//    @Value("${amazon.url}")
//    private String amazonUrl;
//
//    @Value("${aws.s3.bucket.qaclide}")
//    private String bucketName;

    @Value("${tesseract.datapath}")
    private String tessDataPath;

    private static final List<String> ALLOWED_TYPES = List.of(
            "pdf","doc","docx","txt",
            "jpg","jpeg","png","bmp",
            "tiff","tif","gif","webp"
    );


    // ================= UPLOAD SERVICE METHOD =================

    @Override
    @Transactional
    public Upload3and4ResponseDto uploadSection3and4(
            UploadRequestDto request
    ) {

        try {

            // ================= FILE VALIDATION =================

            MultipartFile file = request.getFile();

            if (file == null || file.isEmpty()) {

                throw new RuntimeException(
                        "File is empty"
                );
            }

            String fileName =
                    file.getOriginalFilename();

            String fileType =
                    file.getContentType();

            byte[] fileData =
                    file.getBytes();

            // ================= FILE HASH =================

            String fileHash =
                    generateHash(fileData);

            // ================= DUPLICATE CHECK =================

            Integer duplicateCount =
                    repository.checkDuplicateFile(
                            fileHash
                    );

            if (duplicateCount != null
                    && duplicateCount > 0) {

                throw new RuntimeException(
                        "Duplicate file already uploaded"
                );
            }

            // ================= SDS ID =================
            // Generates: 1,2,3,4,5....

            Long sdsId =
                    repository.generateNextSdsId();

            // ================= TEMP FILE =================

            File tempFile =
                    File.createTempFile(
                            "ocr_",
                            fileName
                    );

            file.transferTo(tempFile);

            // ================= OCR =================

            String text =
                    cleanOcrText(
                            runOCR(tempFile)
                    );

            // ================= SECTION 3 =================

            if ("SECTION_3".equalsIgnoreCase(
                    request.getSectionType()
            )) {

                List<String[]> ingredients =
                        extractIngredients(text);

                for (String[] i : ingredients) {

                    repository.insertIngredient(

                            sdsId,

                            i[0],

                            i[1],

                            i[2],

                            extractMin(i[3]),

                            extractMax(i[3]),

                            i[4],

                            fileData,

                            fileName,

                            fileType,

                            fileHash
                    );
                }

                extractLimits(text)
                        .forEach(l ->

                                repository.insertSpecialLimit(

                                        sdsId,

                                        l[0],

                                        l[1],

                                        l[2]
                                )
                        );
            }

            // ================= SECTION 4 =================

            if ("SECTION_4".equalsIgnoreCase(
                    request.getSectionType()
            )) {

                repository.insertFirstAid(

                        sdsId,

                        safe(
                                extractBlock(
                                        text,
                                        "Emergency Overview",
                                        "General Advice"
                                )
                        ),

                        safe(
                                extractBlock(
                                        text,
                                        "General Advice",
                                        "After Inhalation"
                                )
                        ),

                        safe(
                                extractBlock(
                                        text,
                                        "After Inhalation",
                                        "After Skin Contact"
                                )
                        ),

                        safe(
                                extractBlock(
                                        text,
                                        "After Skin Contact",
                                        "After Eye Contact"
                                )
                        ),

                        safe(
                                extractBlock(
                                        text,
                                        "After Eye Contact",
                                        "After Swallowing"
                                )
                        ),

                        safe(
                                extractBlock(
                                        text,
                                        "After Swallowing",
                                        "Immediate Medical Attention"
                                )
                        ),

                        safe(
                                extractBlock(
                                        text,
                                        "Immediate Medical Attention",
                                        null
                                )
                        ),

                        fileData,

                        fileName,

                        fileType,

                        fileHash
                );

                // ================= SPECIAL TREATMENTS =================

                List<String> treatments =
                        extractTreatments(text);

                for (String treatment : treatments) {

                    if (treatment != null
                            && !treatment.isBlank()) {

                        repository.insertSpecialTreatment(
                                sdsId,
                                treatment
                        );
                    }
                }

                // ================= SYMPTOMS =================

                List<String> symptoms =
                        extractSymptoms(text);

                for (String symptom : symptoms) {

                    if (symptom != null
                            && !symptom.isBlank()) {

                        repository.insertSymptom(
                                sdsId,
                                symptom
                        );
                    }
                }
            }

            // ================= DELETE TEMP FILE =================

            tempFile.delete();

            // ================= RESPONSE =================

            Upload3and4ResponseDto response =
                    new Upload3and4ResponseDto();

            response.setStatus("SUCCESS");

            response.setSdsId(sdsId);

            response.setVersion(1);

            response.setMessage(
                    "Uploaded successfully"
            );

            return response;

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Upload failed : "
                            + e.getMessage()
            );
        }
    }


// ================= OCR METHOD =================
public static String runOCR(File file) {

    try {

        String fileName = file.getName().toLowerCase();

        Tesseract tesseract = new Tesseract();

        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");

        // ================= PDF SUPPORT =================

        if (fileName.endsWith(".pdf")) {

            StringBuilder result = new StringBuilder();

            try (PDDocument document = PDDocument.load(file)) {

                PDFRenderer renderer = new PDFRenderer(document);

                for (int i = 0; i < document.getNumberOfPages(); i++) {

                    BufferedImage image =
                            renderer.renderImageWithDPI(i, 300);

                    result.append(
                            tesseract.doOCR(image)
                    ).append("\n");
                }
            }

            return result.toString();
        }

        // ================= IMAGE SUPPORT =================

        if (
                fileName.endsWith(".png")
                        || fileName.endsWith(".jpg")
                        || fileName.endsWith(".jpeg")
                        || fileName.endsWith(".bmp")
                        || fileName.endsWith(".tiff")
                        || fileName.endsWith(".tif")
        ) {

            return tesseract.doOCR(file);
        }

        // ================= TXT SUPPORT =================

        if (fileName.endsWith(".txt")) {

            return Files.readString(file.toPath());
        }

        // ================= DOCX SUPPORT =================

        if (fileName.endsWith(".docx")) {

            StringBuilder text = new StringBuilder();

            try (FileInputStream fis = new FileInputStream(file);
                 XWPFDocument document = new XWPFDocument(fis)) {

                document.getParagraphs().forEach(p ->
                        text.append(p.getText()).append("\n")
                );
            }

            return text.toString();
        }

        // ================= DOC SUPPORT =================

        if (fileName.endsWith(".doc")) {

            throw new RuntimeException(
                    ".doc format not supported currently. Use .docx instead."
            );
        }

        // ================= UNSUPPORTED =================

        throw new RuntimeException(
                "Unsupported file format"
        );

    } catch (Exception e) {

        throw new RuntimeException(
                "OCR failed : " + e.getMessage()
        );
    }
}


// ================= CLEAN OCR TEXT =================

    public static String cleanOcrText(
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

                    .replaceAll(
                            "\\r",
                            "\n"
                    )

                    .replaceAll(
                            "\\n+",
                            "\n"
                    )

                    .replaceAll(
                            "\\s+",
                            " "
                    )

                    .trim();

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }
    }


// ================= HASH GENERATION =================

    public static String generateHash(
            byte[] fileData
    ) {

        try {

            MessageDigest md =
                    MessageDigest.getInstance(
                            "SHA-256"
                    );

            byte[] hashBytes =
                    md.digest(fileData);

            StringBuilder sb =
                    new StringBuilder();

            for (byte b : hashBytes) {

                sb.append(
                        String.format(
                                "%02x",
                                b
                        )
                );
            }

            return sb.toString();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Hash generation failed"
            );
        }
    }


// ================= SAFE METHOD =================

    public static String safe(
            String value
    ) {

        return value == null
                || value.trim().isEmpty()

                ? null

                : value.trim();
    }


// ================= EXTRACT INGREDIENTS =================

    public static List<String[]> extractIngredients(
            String text
    ) {

        List<String[]> list =
                new ArrayList<>();

        Pattern pattern =
                Pattern.compile(

                        "([A-Za-z\\s]+)\\s+"
                                +
                                "(\\d{2,7}-\\d{2}-\\d)\\s*"
                                +
                                "(\\d{3}-\\d{3}-\\d)?\\s*"
                                +
                                "(\\d+\\-\\d+%|\\d+%)?\\s*"
                                +
                                "(H\\d{3}[^\\n]*)?",

                        Pattern.CASE_INSENSITIVE
                );

        Matcher matcher =
                pattern.matcher(text);

        while (matcher.find()) {

            String concentration =
                    safe(
                            matcher.group(4)
                    );

            list.add(

                    new String[]{

                            safe(
                                    matcher.group(1)
                            ),

                            safe(
                                    matcher.group(2)
                            ),

                            safe(
                                    matcher.group(3)
                            ),

                            concentration,

                            safe(
                                    matcher.group(5)
                            )
                    }
            );
        }

        return list;
    }


// ================= EXTRACT LIMITS =================

    public static List<String[]> extractLimits(
            String text
    ) {

        List<String[]> list =
                new ArrayList<>();

        Pattern pattern =
                Pattern.compile(

                        "([A-Za-z\\s]+)\\s+"
                                +
                                "(\\d+%|\\d+\\.\\d+%)\\s+"
                                +
                                "(H\\d{3}.*?)",

                        Pattern.CASE_INSENSITIVE
                );

        Matcher matcher =
                pattern.matcher(text);

        while (matcher.find()) {

            list.add(

                    new String[]{

                            safe(
                                    matcher.group(1)
                            ),

                            safe(
                                    matcher.group(2)
                            ),

                            safe(
                                    matcher.group(3)
                            )
                    }
            );
        }

        return list;
    }


// ================= EXTRACT BLOCK =================

    public static String extractBlock(

            String text,

            String start,

            String end
    ) {

        try {

            if (text == null) {
                return null;
            }

            String lowerText =
                    text.toLowerCase();

            int startIndex =
                    lowerText.indexOf(
                            start.toLowerCase()
                    );

            if (startIndex == -1) {
                return null;
            }

            int endIndex;

            if (end != null) {

                endIndex =
                        lowerText.indexOf(
                                end.toLowerCase(),
                                startIndex
                        );

                if (endIndex == -1) {
                    endIndex =
                            text.length();
                }

            } else {

                endIndex =
                        text.length();
            }

            return text.substring(
                    startIndex,
                    endIndex
            ).trim();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }


// ================= EXTRACT MIN =================

    public static Double extractMin(
            String value
    ) {

        try {

            if (value == null) {
                return null;
            }

            value =
                    value.replace(
                            "%",
                            ""
                    ).trim();

            if (value.contains("-")) {

                return Double.parseDouble(
                        value.split("-")[0]
                );
            }

            return Double.parseDouble(value);

        } catch (Exception e) {

            return null;
        }
    }


// ================= EXTRACT MAX =================

    public static Double extractMax(
            String value
    ) {

        try {

            if (value == null) {
                return null;
            }

            value =
                    value.replace(
                            "%",
                            ""
                    ).trim();

            if (value.contains("-")) {

                return Double.parseDouble(
                        value.split("-")[1]
                );
            }

            return Double.parseDouble(value);

        } catch (Exception e) {

            return null;
        }
    }


// ================= EXTRACT TREATMENTS =================

    public static List<String> extractTreatments(
            String text
    ) {

        List<String> list =
                new ArrayList<>();

        Pattern pattern =
                Pattern.compile(

                        "(Immediate medical attention.*?)(\\.|\\n)",

                        Pattern.CASE_INSENSITIVE
                );

        Matcher matcher =
                pattern.matcher(text);

        while (matcher.find()) {

            String treatment =
                    safe(
                            matcher.group(1)
                    );

            if (treatment != null) {

                list.add(treatment);
            }
        }

        return list;
    }


// ================= EXTRACT SYMPTOMS =================

    public static List<String> extractSymptoms(
            String text
    ) {

        List<String> list =
                new ArrayList<>();

        Pattern pattern =
                Pattern.compile(

                        "(symptoms.*?)(\\.|\\n)",

                        Pattern.CASE_INSENSITIVE
                );

        Matcher matcher =
                pattern.matcher(text);

        while (matcher.find()) {

            String symptom =
                    safe(
                            matcher.group(1)
                    );

            if (symptom != null) {

                list.add(symptom);
            }
        }

        return list;
    }

//    //Upload
//    @Override
//    @Transactional
//    public Upload3and4ResponseDto uploadSection3and4(UploadRequestDto request) {
//
//        try {
//
//            // File validation
//            MultipartFile file = request.getFile();
//
//            if (file == null || file.isEmpty()) {
//                throw new RuntimeException("File is empty");
//            }
//
//            String fileName = file.getOriginalFilename();
//            String fileType = file.getContentType();
//            byte[] fileData = file.getBytes();
//
//            String fileHash = generateHash(fileData);
//
//            if (repository.checkDuplicateFile(fileHash) > 0) {
//                throw new RuntimeException("Duplicate file already uploaded");
//            }
//
//            Long sdsId = System.currentTimeMillis();
//
//            File tempFile = File.createTempFile("ocr_", fileName);
//            file.transferTo(tempFile);
//
//            String text = cleanOcrText(runOCR(tempFile));
//
//            if ("SECTION_3".equalsIgnoreCase(request.getSectionType())) {
//
//                List<String[]> ingredients = extractIngredients(text);
//
//                for (String[] i : ingredients) {
//
//                    repository.insertIngredient(
//                            sdsId,
//                            i[0],
//                            i[1],
//                            i[2],
//                            extractMin(i[3]),
//                            extractMax(i[3]),
//                            i[4],
//                            fileData,
//                            fileName,
//                            fileType,
//                            fileHash
//                    );
//
//                }
//
//                extractLimits(text).forEach(l ->
//                        repository.insertSpecialLimit(
//                                sdsId,
//                                l[0],
//                                l[1],
//                                l[2]
//                        )
//                );
//            }
//
//            if ("SECTION_4".equalsIgnoreCase(request.getSectionType())) {
//                repository.insertFirstAid(
//                        sdsId,
//                        extractBlock(text, "Emergency Overview", "General Advice"),
//                        extractBlock(text, "General Advice", "After Inhalation"),
//                        extractBlock(text, "After Inhalation", "After Skin Contact"),
//                        extractBlock(text, "After Skin Contact", "After Eye Contact"),
//                        extractBlock(text, "After Eye Contact", "After Swallowing"),
//                        extractBlock(text, "After Swallowing", "Immediate Medical Attention"),
//                        extractBlock(text, "Immediate Medical Attention", null),
//                        fileData,
//                        fileName,
//                        fileType,
//                        fileHash
//                );
//                extractTreatments(text)
//                        .forEach(t -> repository.insertSpecialTreatment(sdsId, t));
//                extractSymptoms(text)
//                        .forEach(s -> repository.insertSymptom(sdsId, s));
//            }
//            tempFile.delete();
//
//            Upload3and4ResponseDto response = new Upload3and4ResponseDto();
//            response.setStatus("SUCCESS");
//            response.setSdsId(sdsId);
//            response.setVersion(1);
//            response.setMessage("Uploaded successfully");
//            return response;
//        } catch (Exception e) {
//            throw new RuntimeException("Upload failed : " + e.getMessage());
//        }
//    }
//
//
//// Helper methods
//    public static String runOCR(File file) {
//        try {
//            Tesseract tesseract = new Tesseract();
//            tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
//
//            if (file.getName().toLowerCase().endsWith(".pdf")) {
//                StringBuilder result = new StringBuilder();
//                try (PDDocument document = PDDocument.load(file)) {
//                    PDFRenderer renderer = new PDFRenderer(document);
//                    for (int i = 0; i < document.getNumberOfPages(); i++) {
//                        BufferedImage image = renderer.renderImageWithDPI(i, 300);
//                        result.append(tesseract.doOCR(image)).append("\n");
//                    }
//                }
//                return result.toString();
//            }
//
//            return tesseract.doOCR(file);
//
//        } catch (Exception e) {
//            throw new RuntimeException("OCR failed : " + e.getMessage());
//        }
//    }
//
//    public static String cleanOcrText(String text) {
//
//        if (text == null) return "";
//
//        return text
//                .replaceAll("[^\\x00-\\x7F]", " ")
//                .replaceAll("\\r", "\n")
//                .replaceAll("\\n+", "\n")
//                .replaceAll("\\s+", " ")
//                .trim();
//    }
//
//    public static String generateHash(byte[] fileData) {
//
//        try {
//
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//
//            byte[] hashBytes = md.digest(fileData);
//
//            StringBuilder sb = new StringBuilder();
//
//            for (byte b : hashBytes) {
//                sb.append(String.format("%02x", b));
//            }
//
//            return sb.toString();
//
//        } catch (Exception e) {
//            throw new RuntimeException("Hash generation failed");
//        }
//    }
//
//    public static String safe(String value) {
//        return value == null || value.trim().isEmpty() ? null : value.trim();
//    }
//
//    public static List<String[]> extractIngredients(String text) {
//
//        List<String[]> list = new ArrayList<>();
//
//        Pattern pattern = Pattern.compile(
//                "([A-Za-z\\s]+)\\s+" +
//                        "(\\d{2,7}-\\d{2}-\\d)\\s*" +
//                        "(\\d{3}-\\d{3}-\\d)?\\s*" +
//                        "(\\d+\\-\\d+%|\\d+%)?\\s*" +
//                        "(H\\d{3}[^\\n]*)?",
//                Pattern.CASE_INSENSITIVE
//        );
//
//        Matcher matcher = pattern.matcher(text);
//
//        while (matcher.find()) {
//
//            String concentration = safe(matcher.group(4));
//
//            list.add(new String[]{
//                    safe(matcher.group(1)),
//                    safe(matcher.group(2)),
//                    safe(matcher.group(3)),
//                    concentration,
//                    safe(matcher.group(5))
//            });
//        }
//
//        return list;
//    }
//
//    public static List<String[]> extractLimits(String text) {
//
//        List<String[]> list = new ArrayList<>();
//
//        Pattern pattern = Pattern.compile(
//                "([A-Za-z\\s]+)\\s+" +
//                        "(\\d+%|\\d+\\.\\d+%)\\s+" +
//                        "(H\\d{3}.*?)",
//                Pattern.CASE_INSENSITIVE
//        );
//
//        Matcher matcher = pattern.matcher(text);
//
//        while (matcher.find()) {
//
//            list.add(new String[]{
//                    safe(matcher.group(1)),
//                    safe(matcher.group(2)),
//                    safe(matcher.group(3))
//            });
//        }
//
//        return list;
//    }
//
//    public static String extractBlock(String text, String start, String end) {
//
//        if (text == null) return null;
//
//        int startIndex = text.toLowerCase().indexOf(start.toLowerCase());
//
//        if (startIndex == -1) return null;
//
//        int endIndex = end != null
//                ? text.toLowerCase().indexOf(end.toLowerCase(), startIndex)
//                : text.length();
//
//        if (endIndex == -1) endIndex = text.length();
//
//        return text.substring(startIndex, endIndex).trim();
//    }
//
//    public static Double extractMin(String value) {
//
//        try {
//
//            if (value == null) return null;
//
//            value = value.replace("%", "").trim();
//
//            if (value.contains("-")) {
//                return Double.parseDouble(value.split("-")[0]);
//            }
//
//            return Double.parseDouble(value);
//
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public static Double extractMax(String value) {
//
//        try {
//
//            if (value == null) return null;
//
//            value = value.replace("%", "").trim();
//
//            if (value.contains("-")) {
//                return Double.parseDouble(value.split("-")[1]);
//            }
//
//            return Double.parseDouble(value);
//
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public static List<String> extractTreatments(String text) {
//
//        List<String> list = new ArrayList<>();
//
//        Pattern pattern = Pattern.compile(
//                "(Immediate medical attention.*?)(\\.|\\n)",
//                Pattern.CASE_INSENSITIVE
//        );
//
//        Matcher matcher = pattern.matcher(text);
//
//        while (matcher.find()) {
//            list.add(safe(matcher.group(1)));
//        }
//
//        return list;
//    }
//
//    public static List<String> extractSymptoms(String text) {
//
//        List<String> list = new ArrayList<>();
//
//        Pattern pattern = Pattern.compile(
//                "(symptoms.*?)(\\.|\\n)",
//                Pattern.CASE_INSENSITIVE
//        );
//
//        Matcher matcher = pattern.matcher(text);
//
//        while (matcher.find()) {
//            list.add(safe(matcher.group(1)));
//        }
//
//        return list;
//    }



    // GET Ingredients by SDS
    @Override
    public List<IngredientResponseDTO> getIngredientsBySdsId(Long sdsId) {
        List<Ingredient> ingredients = ingredientRepository.findBySdsIdAndIsActiveTrue(sdsId);

        return ingredients.stream()
                .map(ingredient -> {

                    String concentration;

                    Double min = ingredient.getConcentrationMin();
                    Double max = ingredient.getConcentrationMax();

                    if (min == null && max == null) {
                        concentration = null;
                    } else if (min != null && max != null) {
                        concentration = min.intValue() + " - " + max.intValue() + "%";
                    } else if (min != null) {
                        concentration = "≥ " + min.intValue() + "%";
                    } else {
                        concentration = "< " + max.intValue() + "%";
                    }

                    return IngredientResponseDTO.builder()
                            .id(ingredient.getId())
                            .chemicalName(ingredient.getChemicalName())
                            .casNumber(ingredient.getCasNumber())
                            .ecNumber(ingredient.getEcNumber())
                            .concentration(concentration)
                            .classification(ingredient.getClassification())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // UPDATE Ingredient
    @Override
    public IngredientResponseDTO updateIngredient(Long ingredientId, UpdateIngredientRequestDTO request) {

        // 1. Fetch ingredient from DB
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + ingredientId));

        // 2. Update fields
        ingredient.setChemicalName(request.getChemicalName());
        ingredient.setCasNumber(request.getCasNumber());
        ingredient.setEcNumber(request.getEcNumber());
        ingredient.setConcentrationMin(request.getConcentrationMin());
        ingredient.setConcentrationMax(request.getConcentrationMax());
        ingredient.setClassification(request.getClassification());

        // 3. Update timestamp
        ingredient.setUpdatedAt(java.time.LocalDateTime.now());

        // 4. Save updated entity
        Ingredient saved = ingredientRepository.save(ingredient);

        // 5. Convert to ResponseDTO
        String concentration;

        if (saved.getConcentrationMin() == null && saved.getConcentrationMax() == null) {
            concentration = null;
        } else if (saved.getConcentrationMin() != null && saved.getConcentrationMax() != null) {
            concentration = saved.getConcentrationMin().intValue() + " - " +
                    saved.getConcentrationMax().intValue() + "%";
        } else if (saved.getConcentrationMin() != null) {
            concentration = "≥ " + saved.getConcentrationMin().intValue() + "%";
        } else {
            concentration = "< " + saved.getConcentrationMax().intValue() + "%";
        }

        return IngredientResponseDTO.builder()
                .id(saved.getId())
                .chemicalName(saved.getChemicalName())
                .casNumber(saved.getCasNumber())
                .ecNumber(saved.getEcNumber())
                .concentration(concentration)
                .classification(saved.getClassification())
                .build();
    }

    // Delete Ingredients
    @Override
    public void deleteIngredient(Long ingredientId) {

        // 1. Fetch ingredient
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + ingredientId));

        // 2. Soft delete (mark inactive)
        ingredient.setIsActive(false);

        // 3. Update timestamp
        ingredient.setUpdatedAt(java.time.LocalDateTime.now());

        // 4. Save changes
        ingredientRepository.save(ingredient);
    }

    // Create Special Limit
    @Override
    public SpecialLimitResponseDTO createSpecialLimit(SpecialLimitRequestDTO request) {

        // 1. Convert DTO → Entity
        SpecialLimit specialLimit = SpecialLimit.builder()
                .sdsId(request.getSdsId())
                .substanceName(request.getSubstanceName())
                .limitValue(request.getLimitValue())
                .hazardClass(request.getHazardClass())
                .remarks(request.getRemarks())
                .isActive(true)
                .build();

        // 2. Save to DB
        SpecialLimit saved = specialLimitRepository.save(specialLimit);

        // 3. Convert Entity → ResponseDTO
        return SpecialLimitResponseDTO.builder()
                .id(saved.getId())
                .sdsId(saved.getSdsId())
                .substanceName(saved.getSubstanceName())
                .limitValue(saved.getLimitValue())
                .hazardClass(saved.getHazardClass())
                .remarks(saved.getRemarks())
                .build();
    }

    // GET Special Limits
    @Override
    public List<SpecialLimitResponseDTO> getSpecialLimitsBySdsId(Long sdsId) {

        // 1. Fetch from DB (only active records)
        List<SpecialLimit> specialLimits =
                specialLimitRepository.findBySdsIdAndIsActiveTrue(sdsId);

        // 2. Convert Entity → DTO
        return specialLimits.stream()
                .map(limit -> SpecialLimitResponseDTO.builder()
                        .id(limit.getId())
                        .sdsId(limit.getSdsId())
                        .substanceName(limit.getSubstanceName())
                        .limitValue(limit.getLimitValue())
                        .hazardClass(limit.getHazardClass())
                        .remarks(limit.getRemarks())
                        .build())
                .toList();
    }
}





