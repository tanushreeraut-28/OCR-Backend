package com.clideOffice.clideApp.common.ocr_project.sds.Section3.serviceImpl;

import com.amazonaws.services.s3.AmazonS3;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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


    //Upload
    @Override
    public Upload3and4ResponseDto uploadSection3and4(UploadRequestDto request) {

        Upload3and4ResponseDto response = new Upload3and4ResponseDto();

        try {
            MultipartFile file = request.getFile();

            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            // ================= CREATE FOLDER =================
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
            File folder = new File(uploadDir);

            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    throw new RuntimeException("Failed to create upload directory");
                }
            }

            // ================= FILE NAME =================
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                throw new RuntimeException("Invalid file name");
            }

            // ================= SDS ID =================
            Long sdsId = System.currentTimeMillis();

            // ================= EXTENSION =================
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // ================= FINAL FILE NAME =================
            String finalFileName = sdsId + "_" + request.getSectionType().toLowerCase() + fileExtension;

            File savedFile = new File(folder, finalFileName);

            // ================= SAVE FILE =================
            try (InputStream in = file.getInputStream();
                 FileOutputStream out = new FileOutputStream(savedFile)) {

                byte[] buffer = new byte[8192];
                int bytesRead;

                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            // ================= OCR =================
            String rawText = runOCR(savedFile);
            String text = cleanOcrText(rawText);

            // 🔥 DEBUG (IMPORTANT)
            System.out.println("==== OCR TEXT START ====");
            System.out.println(text);
            System.out.println("==== OCR TEXT END ====");

            // ================= SECTION 3 =================
            if ("SECTION_3".equalsIgnoreCase(request.getSectionType())) {

                List<String[]> ingredients = extractIngredients(text);

                // 🔥 DEBUG
                System.out.println("INGREDIENT COUNT: " + ingredients.size());

                for (String[] i : ingredients) {

                    // 🔥 FIX: normalize CAS number
                    String cas = i[1] != null ? i[1].replaceAll("\\s+", "-") : null;

                    // ❗ TEMP FIX: skip duplicate check
                    repository.insertIngredient(
                            sdsId,
                            i[0],                // chemical_name
                            cas,                 // fixed CAS
                            i[2],                // ec_number
                            null, null,
                            i[3]                 // classification
                    );
                }

                extractLimits(text).forEach(l ->
                        repository.insertSpecialLimit(
                                sdsId,
                                l[0], l[1], null, null
                        )
                );
            }

            // ================= SECTION 4 =================
            if ("SECTION_4".equalsIgnoreCase(request.getSectionType())) {

                repository.insertFirstAid(
                        sdsId,
                        extractBlock(text, "Emergency Overview", "General Advice"),
                        extractBlock(text, "General Advice", "After Inhalation"),
                        extractBlock(text, "After Inhalation", "After Skin Contact"),
                        extractBlock(text, "After Skin Contact", "After Eye Contact"),
                        extractBlock(text, "After Eye Contact", "After Swallowing"),
                        extractBlock(text, "After Swallowing", "Immediate Medical Attention"),
                        extractBlock(text, "Immediate Medical Attention", null)
                );

                extractTreatments(text).forEach(t ->
                        repository.insertSpecialTreatment(sdsId, t)
                );
            }

            // ================= S3 UPLOAD =================
            String s3Key = "sds/" + finalFileName;
            amazonS3.putObject(bucketName, s3Key, savedFile);

            String fileUrl = "https://" + bucketName + ".s3.ap-south-1.amazonaws.com/" + s3Key;

            // ================= RESPONSE =================
            response.setStatus("SUCCESS");
            response.setSdsId(sdsId);
            response.setVersion(1);
            response.setFileUrl(fileUrl);
            response.setMessage("Uploaded successfully");

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }

    // Helper Code
    // ================= OCR METHOD =================
    public static String runOCR(File file) {
        try {
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");

            String fileName = file.getName().toLowerCase();

            // ✅ IF PDF → convert to images manually
            if (fileName.endsWith(".pdf")) {

                StringBuilder result = new StringBuilder();

                // 👉 PDFBox 2.x compatible
                try (org.apache.pdfbox.pdmodel.PDDocument document =
                             org.apache.pdfbox.pdmodel.PDDocument.load(file)) {

                    org.apache.pdfbox.rendering.PDFRenderer renderer =
                            new org.apache.pdfbox.rendering.PDFRenderer(document);

                    for (int i = 0; i < document.getNumberOfPages(); i++) {

                        java.awt.image.BufferedImage image =
                                renderer.renderImageWithDPI(i, 300);

                        result.append(tesseract.doOCR(image)).append("\n");
                    }
                }

                return result.toString();
            }

            // ✅ IF IMAGE → direct OCR
            return tesseract.doOCR(file);

        } catch (Exception e) {
            throw new RuntimeException("OCR failed: " + e.getMessage());
        }
    }

    // ================= CLEAN OCR TEXT =================
    public static String cleanOcrText(String text) {

        if (text == null) return "";

        return text
                .replaceAll("[^\\x00-\\x7F]", " ")
                .replaceAll("\\r", "\n")
                .replaceAll("\\n+", "\n")
                .replaceAll("\\s+", " ")
                .trim();
    }

    // ================= SAFE NULL HANDLER =================
    public static String safe(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }

    // ================= EXTRACT INGREDIENTS =================
    public static List<String[]> extractIngredients(String text) {

        List<String[]> list = new ArrayList<>();
        if (text == null || text.isEmpty()) return list;

        Pattern pattern = Pattern.compile(
                "([A-Za-z\\s\\-]+)\\s+(\\d{2,7}-\\d{2}-\\d).*?(\\d+%|\\d+\\.\\d+%)?.*?(H\\d{3}.*?)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher m = pattern.matcher(text);

        while (m.find()) {
            list.add(new String[]{
                    safe(m.group(1)), // chemical_name
                    safe(m.group(2)), // cas_number
                    safe(m.group(3)), // concentration
                    safe(m.group(4))  // classification
            });
        }

        return list;
    }

    // ================= EXTRACT SPECIAL LIMITS =================
    public static List<String[]> extractLimits(String text) {

        List<String[]> list = new ArrayList<>();
        if (text == null || text.isEmpty()) return list;

        Pattern pattern = Pattern.compile(
                "([A-Za-z\\s]+)\\s+(\\d+%|\\d+\\.\\d+%)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher m = pattern.matcher(text);

        while (m.find()) {
            list.add(new String[]{
                    safe(m.group(1)), // substance_name
                    safe(m.group(2))  // limit_value
            });
        }

        return list;
    }

    // ================= EXTRACT TEXT BLOCK =================
    public static String extractBlock(String text, String start, String end) {

        if (text == null) return null;

        int startIndex = text.toLowerCase().indexOf(start.toLowerCase());
        if (startIndex == -1) return null;

        int endIndex;

        if (end != null) {
            endIndex = text.toLowerCase().indexOf(end.toLowerCase(), startIndex);
            if (endIndex == -1) endIndex = text.length();
        } else {
            endIndex = text.length();
        }

        return text.substring(startIndex, endIndex).trim();
    }

    // ================= EXTRACT SPECIAL TREATMENTS =================
    public static List<String> extractTreatments(String text) {

        List<String> list = new ArrayList<>();
        if (text == null || text.isEmpty()) return list;

        Pattern pattern = Pattern.compile(
                "(Immediate medical attention.*?)(\\.|\\n)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher m = pattern.matcher(text);

        while (m.find()) {
            list.add(safe(m.group(1)));
        }

        return list;
    }



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





//package com.clideOffice.clideApp.common.ocr_project.sds.Section3.serviceImpl;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.clideOffice.clideApp.common.ocr_project.sds.Section3.service.Section3Service;
//import com.clideOffice.clideApp.common.ocr_project.sds.entity.section3.Ingredient;
//import com.clideOffice.clideApp.common.ocr_project.sds.entity.section3.SpecialLimit;
//import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.UploadProjection3and4;
//import com.clideOffice.clideApp.common.ocr_project.sds.repository.IngredientRepository;
//import com.clideOffice.clideApp.common.ocr_project.sds.repository.SpecialLimitRepository;
//import com.clideOffice.clideApp.common.ocr_project.sds.repository.UploadRepository3and4;
//import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.*;
//import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.IngredientResponseDTO;
//
//import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SpecialLimitResponseDTO;
//import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Upload3and4ResponseDto;
//import lombok.RequiredArgsConstructor;
//
//import net.sourceforge.tess4j.Tesseract;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.rendering.PDFRenderer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class Section3ServiceImpl implements Section3Service {
//
//    private final IngredientRepository ingredientRepository;
//    private final SpecialLimitRepository specialLimitRepository;
//    private final UploadRepository3and4 repository;
//
//    private final AmazonS3 amazonS3;
//
//    @Value("${amazon.url}")
//    private String amazonUrl;
//
//    @Value("${aws.s3.bucket.qaclide}")
//    private String bucketName;
//
//    @Value("${tesseract.datapath}")
//    private String tessDataPath;
//
//    private static final List<String> ALLOWED_TYPES = List.of(
//            "pdf", "doc", "docx", "txt",
//            "jpg", "jpeg", "png", "bmp",
//            "tiff", "tif", "gif", "webp"
//    );
//
//    // Upload
//    @Override
//    public Upload3and4ResponseDto uploadSection3and4(UploadRequestDto request) {
//
//        Upload3and4ResponseDto response = new Upload3and4ResponseDto();
//
//        try {
//            MultipartFile file = request.getFile();
//
//            if (file == null || file.isEmpty()) {
//                throw new RuntimeException("File is empty");
//            }
//
//            // ================= CREATE FOLDER =================
//            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
//            File folder = new File(uploadDir);
//
//            if (!folder.exists()) {
//                boolean created = folder.mkdirs();
//                if (!created) {
//                    throw new RuntimeException("Failed to create upload directory");
//                }
//            }
//
//            // ================= FILE NAME =================
//            String originalFileName = file.getOriginalFilename();
//            if (originalFileName == null) {
//                throw new RuntimeException("Invalid file name");
//            }
//
//            // ================= DUPLICATE CHECK =================
//            File savedFile = new File(folder, originalFileName);
//
//            if (savedFile.exists()) {
//                response.setStatus("FAILED");
//                response.setMessage("Duplicate file: already uploaded");
//                return response;
//            }
