package com.clideOffice.clideApp.common.ocr_project.sds.Section1and2.serviceImpl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section1.SdsMaster;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section1.SdsVersion;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section2.HazardPictogram;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section2.HazardStatement;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section2.PrecautionaryStatement;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section1.SdsOcrResult;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section2.SdsSection2Hazard;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.CreateVersionProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.DashboardSummaryProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.DetailsProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.GetPlantProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.GetServiceProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.HazardMasterProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.OcrExtractedDataProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.UploadProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.UploadRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.ConfirmOCRRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.CreateVersionRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.DashboardSummaryRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.DetailsRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.GetPlantRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.GetServiceRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.HandleHazardOcrRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.HazardPictogramRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.HazardStatementRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.OcrExtractedDataRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.PrecautionaryStatementRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.RemoveUserRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.SdsOcrResultRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.SdsVersionRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.UpdateSection1Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.CreateVersionRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.HandleHazardOcrRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UpdateSection1RequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.ConfirmOCRResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.CreateVersionResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DashboardSummaryResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DeleteHazardChildResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DetailsResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetPlantResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetServiceResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.HandleHazardOcrResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.HazardMasterResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.HazardPictogramDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.HazardStatementDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.OcrExtractedDataResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.OcrExtractedDataSection2ResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.PrecautionaryStatementDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.RemoveUserResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section1ResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UpdateSection1ResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.Section1and2.service.SdsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

@Service
@RequiredArgsConstructor
@Slf4j
public class SdsServiceImpl implements SdsService {

//    private final UploadRepository uploadRepository;
    private final UpdateSection1Repository updateSection1Repository;
    private final RemoveUserRepository removeUserRepository;
    private final OcrExtractedDataRepository repository;
    private final GetServiceRepository getServiceRepository;
    private final GetPlantRepository getPlantRepository;
    private final DetailsRepository detailsRepository;
    private final DashboardSummaryRepository dashboardSummaryRepository;
    private final ConfirmOCRRepository confirmOCRRepository;
    private final CreateVersionRepository createVersionRepository;
    private final SdsVersionRepository sdsVersionRepository;
    private final HazardStatementRepository hazardStatementRepository;
    private final PrecautionaryStatementRepository precautionaryStatementRepository;
    private final HazardPictogramRepository hazardPictogramRepository;
    private final HandleHazardOcrRepository hazardRepository;
    private final SdsOcrResultRepository ocrRepository;

//    private final AmazonS3 amazonS3;
//
//    @Value("${amazon.url}")
//    private String amazonUrl;
//
//    @Value("${aws.s3.bucket.qaclide}")
//    private String bucketName;

    @Value("${tesseract.datapath}")
    private String tessDataPath;

//    private static final List<String> ALLOWED_TYPES = List.of(
//            "pdf","doc","docx","txt",
//            "jpg","jpeg","png","bmp",
//            "tiff","tif","gif","webp"
//    );

    // Used In HandleHazardOcr API
    private static final String PREVIEW = "preview";
    private static final String CONFIRM = "confirm";

//    // Upload API
//    @Override
//    public UploadResponseDto uploadSds(UploadRequestDto request) {
//
//        UploadResponseDto response = new UploadResponseDto();
//
//        try {
//            MultipartFile file = request.getFile();
//
//            if (file == null || file.isEmpty()) {
//                throw new RuntimeException("File is empty");
//            }
//
//            validateFileType(file);
//
//            String originalName = file.getOriginalFilename();
//            if (originalName == null) {
//                throw new RuntimeException("Invalid file name");
//            }
//
//            originalName = originalName
//                    .replace("\"", "")
//                    .replace("'", "")
//                    .replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
//
//            String fileName = System.currentTimeMillis() + "_" + originalName;
//            String s3Key = "sds/" + fileName;
//
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(file.getSize());
//            metadata.setContentType(file.getContentType());
//
//            amazonS3.putObject(
//                    new PutObjectRequest(
//                            bucketName,
//                            s3Key,
//                            new ByteArrayInputStream(file.getBytes()),
//                            metadata
//                    )
//            );
//
//            String fileUrl = amazonS3.getUrl(bucketName, s3Key).toString();
//
//            String rawText = runOCR(file);
//            String cleanedText = cleanOcrText(rawText);
//            double confidenceScore = 0.90;
//
//            // ✅ Detect Section
//            String sectionType = detectSectionType(cleanedText);
//
//            if ("UNKNOWN".equals(sectionType)) {
//                response.setStatus("OCR_FAILED");
//                response.setMessage("Unable to detect section type");
//                response.setFileUrl(fileUrl);
//                return response;
//            }
//
//            String extractedProduct = extractProductIdentifier(cleanedText);
//            String productIdentifier = normalizeText(extractedProduct);
//
//            String sdsNumber = extractSdsNumber(cleanedText);
//
//            if (sdsNumber == null || sdsNumber.isBlank() || sdsNumber.equalsIgnoreCase("Number")) {
//                sdsNumber = "SDS_" + System.currentTimeMillis();
//            }
//
//            String manufacturerInfo = extractManufacturerInfo(cleanedText);
//
//            if ((productIdentifier == null || productIdentifier.isBlank())
//                    && !"SECTION_2".equals(sectionType)) {
//
//                response.setStatus("OCR_FAILED");
//                response.setMessage("Product Identifier not detected.");
//                response.setFileUrl(fileUrl);
//                return response;
//            }
//
//            if (productIdentifier == null || productIdentifier.isBlank()) {
//                productIdentifier = "UNKNOWN_PRODUCT";
//            }
//
//            UploadProjection duplicate =
//                    uploadRepository.findDuplicateSds(productIdentifier, sdsNumber);
//
//            if (duplicate != null) {
//                uploadRepository.logDuplicate(productIdentifier, duplicate.getSdsId(), "UPLOAD_BLOCKED");
//
//                response.setStatus("DUPLICATE");
//                response.setSdsId(duplicate.getSdsId());
//                response.setMessage("Already exists. Duplicate upload not allowed.");
//                response.setFileUrl(fileUrl);
//                return response;
//            }
//
//            // ✅ Insert master
//            uploadRepository.insertSdsMaster(productIdentifier, sdsNumber, manufacturerInfo, 1L);
//            Long sdsId = uploadRepository.getSdsId(productIdentifier, sdsNumber);
//
//            // ✅ Insert version
//            uploadRepository.insertVersion(sdsId, 1, fileUrl, "Initial Upload", 1L);
//            Long versionId = uploadRepository.getVersionId(sdsId);
//
//            // =========================
//            // ✅ SAFE SECTION HANDLING
//            // =========================
//            try {
//
//                switch (sectionType) {
//
//                    case "SECTION_1":
//                        uploadRepository.insertSection1(
//                                sdsId,
//                                versionId,
//                                productIdentifier,
//                                null,
//                                sdsNumber,
//                                null,
//                                null,
//                                manufacturerInfo,
//                                "OCR"
//                        );
//                        break;
//
//                    case "SECTION_2":
//
//                        //  prevent DB overflow
//                        cleanedText = cleanedText.length() > 2000
//                                ? cleanedText.substring(0, 2000)
//                                : cleanedText;
//
//                        handleSection2(cleanedText, sdsId, versionId);
//                        break;
//                }
//
//            } catch (Exception ex) {
//                System.out.println("❌ Section processing failed: " + ex.getMessage());
//
//                response.setStatus("SECTION_FAILED");
//                response.setMessage("Section processing failed: " + ex.getMessage());
//                response.setSdsId(sdsId);
//                response.setFileUrl(fileUrl);
//                return response;
//            }
//
//            // =========================
//            // ✅ OCR MERGE
//            // =========================
//            SdsOcrResult existingOcr = ocrRepository.findTopBySdsIdOrderByCreatedAtDesc(sdsId);
//            if (existingOcr != null) {
//                rawText = existingOcr.getRawText() + "\n" + rawText;
//            }
//
//            uploadRepository.saveOcrResult(sdsId, versionId, rawText, confidenceScore);
//            uploadRepository.insertAuditLog(sdsId, "UPLOAD", 1L, "New SDS uploaded via OCR");
//
//            response.setStatus("SUCCESS");
//            response.setSdsId(sdsId);
//            response.setVersion(1);
//            response.setFileUrl(fileUrl);
//            response.setMessage("SDS uploaded successfully");
//
//            return response;
//
//        } catch (Exception e) {
//            e.printStackTrace(); // 🔥 VERY IMPORTANT
//            throw new RuntimeException("Upload failed: " + e.getMessage());
//        }
//    }

    // UpdateSection1 API
    @Transactional
    public UpdateSection1ResponseDto updateSection1(Long sdsId, UpdateSection1RequestDto request) {

        Integer versionNumber = updateSection1Repository.getCurrentVersion(sdsId);

        if (versionNumber == null) {
            throw new RuntimeException("SDS not found with id: " + sdsId);
        }

        Long versionId = updateSection1Repository.getVersionId(sdsId, versionNumber);

        if (versionId == null) {
            throw new RuntimeException("Version not found for SDS: " + sdsId);
        }

        int rowsUpdated = updateSection1Repository.updateSection1(
                sdsId,
                versionId,
                request.getProductIdentifier(),
                request.getOtherIdentification(),
                request.getSdsNumber(),
                request.getRecommendedUse(),
                request.getRecommendedRestrictions(),
                request.getManufacturerInfo(),
                request.getSourceType()
        );

        if (rowsUpdated == 0) {
            updateSection1Repository.insertSection1(
                    sdsId,
                    versionId,
                    request.getProductIdentifier(),
                    request.getOtherIdentification(),
                    request.getSdsNumber(),
                    request.getRecommendedUse(),
                    request.getRecommendedRestrictions(),
                    request.getManufacturerInfo(),
                    request.getSourceType()
            );
        }

        // 🔥 Sync with master table
        updateSection1Repository.updateSdsMasterFields(
                sdsId,
                request.getProductIdentifier(),
                request.getSdsNumber(),
                request.getManufacturerInfo()
        );

        updateSection1Repository.updateSdsMasterTimestamp(sdsId);

        UpdateSection1ResponseDto response = new UpdateSection1ResponseDto();
        response.setSdsId(sdsId);
        response.setMessage("Section 1 updated successfully");

        return response;
    }

    // RemoveUser API
    @Override
    public RemoveUserResponseDto removeUser(Integer userId) {

        int updatedRows = removeUserRepository.removeUser(userId);

        if (updatedRows == 0) {
            throw new RuntimeException("User not found");
        }

        RemoveUserResponseDto response = new RemoveUserResponseDto();
        response.setUserId(Long.valueOf(userId));
        response.setMessage("User removed successfully");

        return response;
    }

    //OCRExtractedData API
    @Override
    public OcrExtractedDataResponseDto getOcrExtractedData(Long sdsId) {

        OcrExtractedDataProjection data = repository.getOcrExtractedData(sdsId);

        if (data == null) {
            return OcrExtractedDataResponseDto.builder().message("OCR data not found").build();
        }

        String rawText = data.getRawText();

        // helper function
        String sdsNumber = data.getSdsNumber();
        if (sdsNumber == null) {
            sdsNumber = extractValue(rawText, "SDS Number");
        }

        String use = data.getRecommendedUse();
        if (use == null) {
            use = extractValue(rawText, "Recommended Use");
        }

        String restriction = data.getRecommendedRestrictions();
        if (restriction == null) {
            restriction = extractValue(rawText, "Restriction");
        }

        String manufacturer = data.getManufacturerInfo();
        if (manufacturer == null) {
            manufacturer = extractValue(rawText, "Manufacturer");
        }

        String otherName = data.getOtherIdentification();
        if (otherName == null) {
            otherName = extractValue(rawText, "Other Identification");
        }

        return OcrExtractedDataResponseDto.builder().sdsId(data.getSdsId()).versionId(data.getVersionId())

                .product(data.getProductIdentifier()).otherName(otherName).sdsNumber(sdsNumber).use(use)
                .restriction(restriction).manufacturer(manufacturer)

                .sourceType(data.getSourceType()).message("OCR extracted data fetched successfully").build();
    }

    //GetService API
    @Override
    public List<GetServiceResponseDto> getAllServices() {

        List<GetServiceProjection> services =
                getServiceRepository.getAllServices();

        return services.stream()
                .map(service -> GetServiceResponseDto.builder()
                        .serviceId(service.getServiceId())
                        .serviceName(service.getServiceName())
                        .build())
                .collect(Collectors.toList());
    }


    // Get Plant API
    @Override
    public List<GetPlantResponseDto> getPlantsByService(Long serviceId) {

        List<GetPlantProjection> plants =
                getPlantRepository.getPlantsByService(serviceId);

        return plants.stream()
                .map(p -> new GetPlantResponseDto(
                        p.getPlantId(),
                        p.getPlantName()
                ))
                .collect(Collectors.toList());
    }


    // Details API
    @Override
    public DetailsResponseDto getSdsDetails(Long sdsId) {

        DetailsProjection projection = detailsRepository.getSdsDetails(sdsId);

        if (projection == null) {
            throw new RuntimeException("SDS not found for id : " + sdsId);
        }

        // Section1 mapping
        Section1ResponseDto section1 = Section1ResponseDto.builder()
                .productIdentifier(projection.getProductIdentifier())
                .otherIdentification(projection.getOtherIdentification())
                .sdsNumber(projection.getSdsNumber())
                .recommendedUse(projection.getRecommendedUse())
                .recommendedRestrictions(projection.getRecommendedRestrictions())
                .manufacturerInfo(projection.getManufacturerInfo())
                .sourceType(projection.getSourceType())
                .build();

        // Main response
        DetailsResponseDto response = DetailsResponseDto.builder()
                .sdsId(projection.getSdsId())
                .fileUrl(projection.getFileUrl())
                .version(projection.getVersion())
                .status(projection.getStatus())
                .uploadedAt(projection.getUploadedAt())
                .confidenceScore(projection.getConfidenceScore())
                .section1(section1)
                .build();

        return response;
    }

    // DashBoaredSummary API
    @Override
    public DashboardSummaryResponseDto getDashboardSummary() {

        DashboardSummaryProjection projection =
                dashboardSummaryRepository.getDashboardSummary();

        return DashboardSummaryResponseDto.builder()
                .totalSds(projection.getTotalSds())
                .pendingReview(projection.getPendingReview())
                .duplicates(projection.getDuplicates())
                .build();
    }

    // CreateVersion API
//    @Override
//    @Transactional
//    public CreateVersionResponseDto createVersion(Long sdsId, CreateVersionRequestDto request) {
//
//        CreateVersionProjection current =
//                createVersionRepository.getCurrentVersion(sdsId);
//
//        if (current == null) {
//            throw new RuntimeException("SDS not found");
//        }
//
//        if (request.getFile() == null || request.getFile().isEmpty()) {
//            throw new RuntimeException("File is empty or not provided");
//        }
//
//        Integer newVersion = current.getCurrentVersion() + 1;
//
//        MultipartFile file = request.getFile();
//
//        try {
//
//            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(file.getSize());
//            metadata.setContentType(file.getContentType());
//
//            amazonS3.putObject(
//                    bucketName,
//                    fileName,
//                    file.getInputStream(),
//                    metadata
//            );
//
//            String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();
//
//            createVersionRepository.insertNewVersion(
//                    sdsId,
//                    newVersion,
//                    fileUrl,
//                    request.getChangeNotes(),
//                    request.getUploadedBy()
//            );
//
//            createVersionRepository.updateCurrentVersion(sdsId, newVersion);
//
//            return CreateVersionResponseDto.builder()
//                    .sdsId(sdsId)
//                    .version(newVersion)
//                    .fileUrl(fileUrl)
//                    .message("New version created successfully")
//                    .build();
//
//        } catch (Exception e) {
//            throw new RuntimeException("File upload failed", e);
//        }
//    }

    @Override
    @Transactional
    public CreateVersionResponseDto createVersion(
            Long sdsId,
            CreateVersionRequestDto request) {

        CreateVersionProjection current =
                createVersionRepository.getCurrentVersion(sdsId);

        if (current == null) {
            throw new RuntimeException("SDS not found");
        }

        if (request.getFile() == null ||
                request.getFile().isEmpty()) {

            throw new RuntimeException(
                    "File is empty or not provided");
        }

        Integer newVersion =
                current.getCurrentVersion() + 1;

        MultipartFile file = request.getFile();

        try {

            String fileName =
                    file.getOriginalFilename();

            String fileType =
                    file.getContentType();

            byte[] fileData =
                    file.getBytes();

            createVersionRepository.insertNewVersion(
                    sdsId,
                    newVersion,
                    fileName,
                    fileType,
                    fileData,
                    request.getChangeNotes(),
                    request.getUploadedBy()
            );

            createVersionRepository.updateCurrentVersion(
                    sdsId,
                    newVersion
            );

            return CreateVersionResponseDto.builder()
                    .sdsId(sdsId)
                    .version(newVersion)
                    .message("New version created successfully")
                    .build();

        } catch (Exception e) {

            throw new RuntimeException(
                    "File upload failed",
                    e
            );
        }
    }

    // ConfirmOCR API
    @Override
    public ConfirmOCRResponseDto confirmOCR(Long sdsId) {

        // Update OCR status to CONFIRMED in DB
        int updatedRows = confirmOCRRepository.confirmOcr(sdsId);

        // Check if update failed
        if (updatedRows == 0) {
            throw new RuntimeException("OCR not updated!");
        }

        // Get latest version id for this SDS
        Long versionId = sdsVersionRepository
                .findTopBySdsMasterIdOrderByVersionNumberDesc(sdsId)
                .getId();

        // Return success response with sdsId and versionId
        return ConfirmOCRResponseDto.builder()
                .sdsId(sdsId)
                .versionId(versionId)
                .message("OCR confirmed successfully")
                .build();
    }

    // DeleteHazardChild API (Section 2)
    @Override
    public DeleteHazardChildResponseDto deleteHazardChild(String type, Long id) {

        return switch (type.toLowerCase()) {

            case "statement" -> {
                if (!hazardStatementRepository.existsById(id))
                    throw new RuntimeException("Hazard Statement not found");

                hazardStatementRepository.deleteById(id);
                yield DeleteHazardChildResponseDto.builder()
                        .status("SUCCESS").message("Hazard Statement deleted").build();
            }

            case "precaution" -> {
                if (!precautionaryStatementRepository.existsById(id))
                    throw new RuntimeException("Precautionary Statement not found");

                precautionaryStatementRepository.deleteById(id);
                yield DeleteHazardChildResponseDto.builder()
                        .status("SUCCESS").message("Precautionary Statement deleted").build();
            }

            case "pictogram" -> {
                if (!hazardPictogramRepository.existsById(id))
                    throw new RuntimeException("Hazard Pictogram not found");

                hazardPictogramRepository.deleteById(id);
                yield DeleteHazardChildResponseDto.builder()
                        .status("SUCCESS").message("Hazard Pictogram deleted").build();
            }

            default -> throw new IllegalArgumentException("Invalid type");
        };
    }

    // HandleHazardOcr API (Section 2)
    @Override
    public HandleHazardOcrResponseDto handleHazardOcr(Long sdsId, HandleHazardOcrRequestDto request) {

        // 1. Fetch OCR Data (UPDATED)
        SdsOcrResult ocr = ocrRepository.findTopBySdsIdOrderByCreatedAtDesc(sdsId);

        if (ocr == null || ocr.getRawText() == null) {
            throw new RuntimeException("OCR data not found");
        }

        String rawText = ocr.getRawText();

        // 2. Parse OCR → DTO
        HandleHazardOcrResponseDto dto = parseOcr(rawText);

        // 3. PREVIEW → return only
        if (PREVIEW.equalsIgnoreCase(request.getAction())) {
            dto.setMessage("Preview data fetched successfully");
            return dto;
        }

        // 4. CONFIRM → save to DB
        if (CONFIRM.equalsIgnoreCase(request.getAction())) {

            SdsSection2Hazard hazard = new SdsSection2Hazard();
            hazard.setHazardClassification(dto.getHazardClassification());
            hazard.setSignalWord(dto.getSignalWord());
            hazard.setSourceType("OCR");
            hazard.setCreatedAt(LocalDateTime.now());

            // Map Hazard Statements
            List<HazardStatement> hazardStatements = dto.getHazardStatements()
                    .stream()
                    .map(h -> {
                        HazardStatement hs = new HazardStatement();
                        hs.setCode(h.getCode());
                        hs.setDescription(h.getDescription());
                        hs.setHazard(hazard);
                        return hs;
                    }).toList();

            // Map Precautionary Statements
            List<PrecautionaryStatement> precautionaryStatements = dto.getPrecautionaryStatements()
                    .stream()
                    .map(p -> {
                        PrecautionaryStatement ps = new PrecautionaryStatement();
                        ps.setCode(p.getCode());
                        ps.setDescription(p.getDescription());
                        ps.setHazard(hazard);
                        return ps;
                    }).toList();

            // Map Pictograms
            List<HazardPictogram> pictograms = dto.getPictograms()
                    .stream()
                    .map(p -> {
                        HazardPictogram pic = new HazardPictogram();
                        pic.setCode(p.getCode());
                        pic.setImageUrl(p.getImageUrl());
                        pic.setHazard(hazard);
                        return pic;
                    }).toList();

            // Attach children
            hazard.setHazardStatements(hazardStatements);
            hazard.setPrecautionaryStatements(precautionaryStatements);
            hazard.setPictograms(pictograms);

            // SAVE
            hazardRepository.save(hazard);

            dto.setMessage("Hazard data saved successfully");
            return dto;
        }

        throw new IllegalArgumentException("Invalid action");
    }

    // ================= OCR Parser =================
    private HandleHazardOcrResponseDto parseOcr(String rawText) {

        return HandleHazardOcrResponseDto.builder()
                .hazardClassification("Flammable Liquid")
                .signalWord("Danger")
                .hazardStatements(List.of(
                        HazardStatementDto.builder()
                                .code("H225")
                                .description("Highly flammable liquid")
                                .build()
                ))
                .precautionaryStatements(List.of(
                        PrecautionaryStatementDto.builder()
                                .code("P210")
                                .description("Keep away from heat")
                                .build()
                ))
                .pictograms(List.of(
                        HazardPictogramDto.builder()
                                .code("GHS02")
                                .imageUrl("image-url")
                                .build()
                ))
                .build();
    }

    // Master Hazard Data API (Section 2)
    @Override
    public HazardMasterResponseDTO getHazardMasterData() {

        // Fetch all master hazard data from projection query
        List<HazardMasterProjection> data = hazardRepository.findMasterData();

        // Extract unique hazard classifications
        List<String> classifications = data.stream()
                .map(HazardMasterProjection::getHazardClassification)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // Extract unique signal words
        List<String> signalWords = data.stream()
                .map(HazardMasterProjection::getSignalWord)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // Map hazard statements (code + description)
        List<HazardStatementDto> hazardStatements = data.stream()
                .filter(d -> d.getCode() != null)
                .map(d -> new HazardStatementDto(d.getCode(), d.getDescription()))
                .distinct()
                .toList();

        // Fetch precautionary statements separately (master only)
        List<PrecautionaryStatementDto> precautionaryStatements =
                precautionaryStatementRepository.findByIsMasterTrue()
                        .stream()
                        .map(p -> new PrecautionaryStatementDto(p.getCode(), p.getDescription()))
                        .toList();

        // Build and return response
        return HazardMasterResponseDTO.builder()
                .classifications(classifications)
                .signalWords(signalWords)
                .hazardStatements(hazardStatements)
                .precautionaryStatements(precautionaryStatements)
                .build();
    }

    // OCR Extracted Data (Section 2)
    @Override
    public OcrExtractedDataSection2ResponseDto getOcrExtractedDataSection2(Long sdsId) {

        SdsSection2Hazard hazard = hazardRepository.findBySdsMaster_Id(sdsId)
                .orElse(null);

        if (hazard == null) {
            return OcrExtractedDataSection2ResponseDto.builder()
                    .message("Section 2 data not found in DB")
                    .build();
        }

        // =========================
        // ✅ FIX 1: Hazard Classification CLEAN
        // =========================
        String hazardClassification = hazard.getHazardClassification();

        if (hazardClassification != null) {
            hazardClassification = hazardClassification
                    .replaceAll("Signal Word.*", "")
                    .replaceAll("Hazard Statements.*", "")
                    .replaceAll("Precautionary Statements.*", "")
                    .trim();
        }

        // fallback if still null
        if (hazardClassification == null || hazardClassification.isBlank()) {
            hazardClassification = "Not Available";
        }

        // =========================
        // ✅ Hazard Statements
        // =========================
        String hazardStatements = hazard.getHazardStatements() != null
                ? hazard.getHazardStatements().stream()
                .map(h ->
                        (h.getCode() != null ? h.getCode() : "") +
                                (h.getDescription() != null ? ": " + h.getDescription() : "")
                )
                .filter(s -> !s.isBlank())
                .reduce((a, b) -> a + "\n" + b)
                .orElse(null)
                : null;

        // =========================
        // ✅ Precautionary Statements CLEAN
        // =========================
        String precautionaryStatements = hazard.getPrecautionaryStatements() != null
                ? hazard.getPrecautionaryStatements().stream()
                .map(p ->
                        (p.getCode() != null ? p.getCode() : "") +
                                (p.getDescription() != null ? ": " + p.getDescription() : "")
                )
                .filter(s -> !s.isBlank())
                .map(s -> s.replaceAll("Pictogram.*", "").trim()) // 🔥 remove noise
                .reduce((a, b) -> a + "\n" + b)
                .orElse(null)
                : null;

        // =========================
        // ✅ Pictogram FIX (fallback)
        // =========================
        String pictogramCodes = null;

        if (hazard.getPictograms() != null && !hazard.getPictograms().isEmpty()) {
            pictogramCodes = hazard.getPictograms().stream()
                    .map(p -> p.getCode())
                    .filter(Objects::nonNull)
                    .reduce((a, b) -> a + "," + b)
                    .orElse(null);
        }

        // fallback if OCR failed
        if ((pictogramCodes == null || pictogramCodes.isBlank())
                && hazard.getHazardStatements() != null) {

            pictogramCodes = hazard.getHazardStatements().stream()
                    .map(h -> h.getCode())
                    .filter(Objects::nonNull)
                    .map(code -> {
                        if (code.startsWith("H3")) return "GHS07";
                        if (code.startsWith("H2")) return "GHS02";
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .distinct()
                    .reduce((a, b) -> a + "," + b)
                    .orElse(null);
        }

        // =========================
        // ✅ FINAL RESPONSE
        // =========================
        return OcrExtractedDataSection2ResponseDto.builder()
                .sdsId(hazard.getSdsMaster().getId())
                .versionId(hazard.getVersion() != null
                        ? hazard.getVersion().getId() : null)
                .hazardClassification(hazardClassification)
                .signalWord(hazard.getSignalWord() != null
                        ? hazard.getSignalWord().trim()
                        : null)
                .hazardStatements(hazardStatements)
                .precautionaryStatements(precautionaryStatements)
                .pictogramCodes(pictogramCodes)
                .otherHazards(hazard.getOtherHazards())
                .message("Section 2 data fetched from DB successfully")
                .build();
    }

//    // Helper Code
//    // ✅ Validate file type
//    private void validateFileType(MultipartFile file){
//        String name = file.getOriginalFilename();
//        if(name == null) throw new RuntimeException("Invalid file name");
//        if(!name.contains(".")) throw new RuntimeException("File extension missing");
//
//        String extension = name.substring(name.lastIndexOf(".")+1).toLowerCase();
//        if(!ALLOWED_TYPES.contains(extension)) throw new RuntimeException("Unsupported file type");
//    }

    // ✅ Route OCR
    private String runOCR(MultipartFile file) throws Exception {
        String name = file.getOriginalFilename();
        String extension = name.substring(name.lastIndexOf(".")+1).toLowerCase();

        if(extension.equals("pdf")) return readPdf(file);
        if(extension.equals("txt")) return new String(file.getBytes());
        if(extension.equals("doc") || extension.equals("docx")) return readWord(file);

        return readImage(file);
    }

    // ✅ Image OCR
    private String readImage(MultipartFile file) throws Exception {
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessDataPath);
        BufferedImage image = ImageIO.read(file.getInputStream());
        return tesseract.doOCR(image);
    }

    // ✅ PDF OCR
    private String readPdf(MultipartFile file) throws Exception {
        StringBuilder text = new StringBuilder();

        try(PDDocument document = PDDocument.load(file.getInputStream())){
            PDFRenderer renderer = new PDFRenderer(document);
            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath(tessDataPath);

            for(int i=0;i<document.getNumberOfPages();i++){
                BufferedImage image = renderer.renderImageWithDPI(i,300);
                text.append(tesseract.doOCR(image)).append("\n");
            }
        }
        return text.toString();
    }

    // ✅ Word reader
    private String readWord(MultipartFile file) throws Exception {
        StringBuilder text = new StringBuilder();

        try(XWPFDocument doc = new XWPFDocument(file.getInputStream())){
            doc.getParagraphs().forEach(p -> text.append(p.getText()).append("\n"));
        }
        return text.toString();
    }

    // ✅ Clean OCR text
    private String cleanOcrText(String text){
        if(text == null) return "";
        return text.replaceAll("[^\\x00-\\x7F]", " ")
                .replaceAll("[*]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    // ✅ Normalize for duplicate check
    private String normalizeText(String text){
        if(text == null) return null;
        return text.toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    // ✅ Extract Product Identifier (FIXED)
    private String extractProductIdentifier(String text){

        if(text == null || text.isBlank()) return null;

        Pattern p1 = Pattern.compile(
                "(Product Identifier|Product Name|Chemical Name|Substance Name)\\s*[:\\-]\\s*([^\\n]{2,100})",
                Pattern.CASE_INSENSITIVE
        );

        Matcher m1 = p1.matcher(text);

        if(m1.find()){
            String value = m1.group(2);

            value = value.split("Revision")[0];
            value = value.split("Other")[0];
            value = value.split("Date")[0];
            value = value.split("SDS")[0];

            return value.trim();
        }

        Pattern p2 = Pattern.compile(
                "(Other Identification|Synonym)\\s*[:\\-]\\s*([^\\n]{2,80})",
                Pattern.CASE_INSENSITIVE
        );

        Matcher m2 = p2.matcher(text);

        if(m2.find()){
            return m2.group(2).trim();
        }

        return null;
    }

    // ✅ Extract SDS Number
    private String extractSdsNumber(String text) {
        if (text == null) return null;
        Pattern pattern = Pattern.compile(
                "(?:SDS\\s*(?:No|Number)?|Document\\s*No)\\s*[:\\-]?\\s*([A-Za-z0-9\\-]{3,50})",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim(); // ✅ correct group
        }
        return null;
    }

    // ✅ Extract Manufacturer Info
    private String extractManufacturerInfo(String text) {
        Pattern pattern = Pattern.compile(
                "(Manufacturer|Company|Supplier)\\s*[:\\-]\\s*([A-Za-z0-9 ,.&()\\-]{5,200})",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(2).trim() : null;
    }

    // regex extractor (OCRExtractedData API)
    private String extractValue(String text, String label) {

        if (text == null)
            return null;

        Pattern pattern = Pattern.compile(label + ":\\s*([^\\n]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? matcher.group(1).trim() : null;
    }


    // ================= SECTION 2 Helper Code =================

    // Section extractor
    private void handleSection2(String text, Long sdsId, Long versionId) {

        // =========================
        // ✅ FIX 1: SAFE SECTION EXTRACTION
        // =========================
        String section2Text = extractSection(text, "Section 2");

        if (section2Text == null || section2Text.isBlank()) {
            // 🔥 fallback → use full text
            section2Text = text;
        }

        // =========================
        // ✅ EXTRACT VALUES
        // =========================
        String hazardClassification = extractMultiLineValue(section2Text, "Classification");
        String signalWord = extractValue(section2Text, "Signal Word");
        String otherHazards = extractValue(section2Text, "Other Hazards");

        String hazardStatementsRaw = extractMultiLineValue(section2Text, "Hazard Statements");
        String precautionaryRaw = extractMultiLineValue(section2Text, "Precautionary Statements");

        String pictogramCodes = extractPictogramCodes(section2Text);

        // =========================
        // 🔥 FIX 2: CLEAN DATA BEFORE SAVE
        // =========================
        if (hazardClassification != null) {
            hazardClassification = hazardClassification
                    .split("Signal Word|Hazard Statements|Precautionary Statements")[0]
                    .trim();

            if (hazardClassification.length() > 255) {
                hazardClassification = hazardClassification.substring(0, 255);
            }
        }

        if (signalWord != null) {
            signalWord = signalWord
                    .split("Hazard Statements|Precautionary Statements")[0]
                    .trim();

            if (signalWord.length() > 50) {
                signalWord = signalWord.substring(0, 50);
            }
        }

        if (otherHazards != null && otherHazards.length() > 255) {
            otherHazards = otherHazards.substring(0, 255);
        }

        // =========================
        // 🔥 FIX 3: DEFAULT FALLBACKS (IMPORTANT)
        // =========================
        if (hazardClassification == null || hazardClassification.isBlank()) {
            hazardClassification = "Not Available";
        }

        if (signalWord == null || signalWord.isBlank()) {
            signalWord = "UNKNOWN";
        }

        // =========================
        // ✅ CREATE MAIN ENTITY
        // =========================
        SdsSection2Hazard hazard = new SdsSection2Hazard();

        hazard.setSdsMaster(new SdsMaster(sdsId));
        hazard.setVersion(new SdsVersion(versionId));

        hazard.setHazardClassification(hazardClassification);
        hazard.setSignalWord(signalWord);
        hazard.setOtherHazards(otherHazards);
        hazard.setSourceType("OCR");
        hazard.setCreatedAt(java.time.LocalDateTime.now());
        hazard.setIsMaster(false);

        // ✅ SAVE MAIN ENTITY
        hazard = hazardRepository.save(hazard);

        // =========================
        // ✅ Hazard Statements
        // =========================
        if (hazardStatementsRaw != null) {

            String[] lines = hazardStatementsRaw.split("\\n");

            for (String line : lines) {

                line = line.trim();

                if (line.isEmpty() || line.toLowerCase().contains("precautionary"))
                    continue;

                Matcher m = Pattern.compile("(H\\d{3})\\s*[-:]\\s*(.*)").matcher(line);

                HazardStatement hs = new HazardStatement();
                hs.setHazard(hazard);

                if (m.find()) {
                    hs.setCode(m.group(1));
                    hs.setDescription(m.group(2));
                } else {
                    hs.setDescription(line);
                }

                hs.setIsMaster(false);
                hazardStatementRepository.save(hs);
            }
        }

        // =========================
        // ✅ Precautionary Statements
        // =========================
        if (precautionaryRaw != null) {

            String[] lines = precautionaryRaw.split("\\n");

            for (String line : lines) {

                line = line.trim();

                if (line.isEmpty())
                    continue;

                // 🔥 remove OCR garbage
                line = line.replaceAll("Pictogram.*", "").trim();

                Matcher m = Pattern.compile("(P\\d+(\\+P\\d+)*)\\s*[-:]\\s*(.*)").matcher(line);

                PrecautionaryStatement ps = new PrecautionaryStatement();
                ps.setHazard(hazard);

                if (m.find()) {
                    ps.setCode(m.group(1));
                    ps.setDescription(m.group(3));
                } else {
                    ps.setDescription(line);
                }

                ps.setIsMaster(false);
                precautionaryStatementRepository.save(ps);
            }
        }

        // =========================
        // ✅ Pictograms
        // =========================
        if (pictogramCodes != null && !pictogramCodes.isBlank()) {

            for (String code : pictogramCodes.split(",")) {

                if (code == null || code.isBlank()) continue;

                HazardPictogram pic = new HazardPictogram();
                pic.setHazard(hazard);
                pic.setCode(code.trim());

                hazardPictogramRepository.save(pic);
            }
        }
    }

    // detectSectionType
    private String detectSectionType(String text) {
        if (text == null) return "UNKNOWN";
        text = text.toLowerCase();

        // ✅ detect section 2 properly
        if (text.contains("section 2") || text.contains("hazard identification")) {
            return "SECTION_2";
        }
        if (text.contains("section 1") || text.contains("identification")) {
            return "SECTION_1";
        }
        return "UNKNOWN";
    }

    // Multi-line Extractor  (OCRExtractedData API Section 2)
    private String extractMultiLineValue(String text, String label) {

        if (text == null) return null;

        Pattern pattern;

        if (label.equalsIgnoreCase("Hazard Statements")) {

            pattern = Pattern.compile(
                    "Hazard Statements\\s*:?\\s*([\\s\\S]*?)(?=Precautionary Statements|Pictogram|$)",
                    Pattern.CASE_INSENSITIVE
            );

        } else if (label.equalsIgnoreCase("Precautionary Statements")) {

            pattern = Pattern.compile(
                    "Precautionary Statements\\s*:?\\s*([\\s\\S]*?)(?=Pictogram|$)",
                    Pattern.CASE_INSENSITIVE
            );

        } else if (label.equalsIgnoreCase("Classification")) {

            pattern = Pattern.compile(
                    "Classification\\s*:?\\s*([\\s\\S]*?)(?=Signal Word|Hazard Statements|$)",
                    Pattern.CASE_INSENSITIVE
            );

        } else {
            pattern = Pattern.compile(label + "\\s*:\\s*(.*)", Pattern.CASE_INSENSITIVE);
        }

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return null;
    }

    // Pictogram Extractor (OCRExtractedData API Section 2)
    private String extractPictogramCodes(String text) {

        if (text == null) return null;

        Pattern pattern = Pattern.compile("GHS\\d{2}");
        Matcher matcher = pattern.matcher(text);

        Set<String> codes = new LinkedHashSet<>();

        while (matcher.find()) {
            codes.add(matcher.group());
        }

        return String.join(",", codes);
    }

    // Section Extractor (OCRExtractedData API Section 2)
    private String extractSection(String text, String sectionName) {

        if (text == null) return null;

        Pattern pattern = Pattern.compile(
                "(?i)(section\\s*2[\\s\\S]*?)(section\\s*3|$)",
                Pattern.DOTALL
        );

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        // 🔥 FALLBACK (VERY IMPORTANT)
        if (text.toLowerCase().contains("section 2")) {
            return text; // return full text if section not isolated
        }

        return null;
    }
}