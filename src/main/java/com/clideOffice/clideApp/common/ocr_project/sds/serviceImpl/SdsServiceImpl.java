package com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

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
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.CreateVersionProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.DashboardSummaryProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.DetailsProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.GetPlantProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.GetServiceProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.OcrExtractedDataProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.UploadProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.UploadRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.ConfirmOCRRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.CreateVersionRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.DashboardSummaryRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.DetailsRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.GetPlantRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.GetServiceRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.OcrExtractedDataRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.RemoveUserRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.UpdateSection1Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.CreateVersionRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UpdateSection1RequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.ConfirmOCRResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.CreateVersionResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DashboardSummaryResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DetailsResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetPlantResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetServiceResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.OcrExtractedDataResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.RemoveUserResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section1ResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UpdateSection1ResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.service.SdsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

@Service
@RequiredArgsConstructor
@Slf4j
public class SdsServiceImpl implements SdsService {

    private final UploadRepository uploadRepository;
    private final UpdateSection1Repository updateSection1Repository;
    private final RemoveUserRepository removeUserRepository;
    private final OcrExtractedDataRepository repository;
    private final GetServiceRepository getServiceRepository;
    private final GetPlantRepository getPlantRepository;
    private final DetailsRepository detailsRepository;
    private final DashboardSummaryRepository dashboardSummaryRepository;
    private final ConfirmOCRRepository confirmOCRRepository;
    private final CreateVersionRepository createVersionRepository;
//    private final UploadService uploadService;
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

    //  Upload API 
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

            UploadProjection duplicate =
                    uploadRepository.findDuplicateSds(productIdentifier);

            if(duplicate != null){

                response.setStatus("DUPLICATE");
                response.setSdsId(duplicate.getSdsId());
                response.setMessage("Duplicate SDS detected for product: " + productIdentifier);
                response.setFileUrl(fileUrl);

                return response;
            }

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

    // UpdateSection1 API 
    @Transactional
    public UpdateSection1ResponseDto updateSection1(Long sdsId, UpdateSection1RequestDto request) {

        updateSection1Repository.updateSection1(
                sdsId,
                request.getProductIdentifier(),
                request.getOtherIdentification(),
                request.getSdsNumber(),
                request.getRecommendedUse(),
                request.getRecommendedRestrictions(),
                request.getManufacturerInfo(),
                request.getSourceType()
        );

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
    @Override
    @Transactional
    public CreateVersionResponseDto createVersion(Long sdsId, CreateVersionRequestDto request) {

        CreateVersionProjection current =
                createVersionRepository.getCurrentVersion(sdsId);

        if (current == null) {
            throw new RuntimeException("SDS not found");
        }

        if (request.getFile() == null || request.getFile().isEmpty()) {
            throw new RuntimeException("File is empty or not provided");
        }

        Integer newVersion = current.getCurrentVersion() + 1;

        MultipartFile file = request.getFile();

        try {

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(
                    bucketName,
                    fileName,
                    file.getInputStream(),
                    metadata
            );

            String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();

            createVersionRepository.insertNewVersion(
                    sdsId,
                    newVersion,
                    fileUrl,
                    request.getChangeNotes(),
                    request.getUploadedBy()
            );

            createVersionRepository.updateCurrentVersion(sdsId, newVersion);

            return CreateVersionResponseDto.builder()
                    .sdsId(sdsId)
                    .version(newVersion)
                    .fileUrl(fileUrl)
                    .message("New version created successfully")
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }
    }
    
    // ConfirmOCR API
    @Override
    public ConfirmOCRResponseDto confirmOCR(Long sdsId) {

        // update OCR status to CONFIRMED
        confirmOCRRepository.confirmOcr(sdsId);

        return ConfirmOCRResponseDto.builder()
                .sdsId(sdsId)
                .message("OCR confirmed successfully")
                .build();
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

        Pattern p3 = Pattern.compile(
                "PRODUCT AND COMPANY IDENTIFICATION\\s*\\+?\\s*([A-Z0-9\\- ]{3,40})",
                Pattern.CASE_INSENSITIVE
        );

        Matcher m3 = p3.matcher(text);

        if(m3.find()){
            return m3.group(1).trim();
        }

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

    // regex extractor (OCRExtractedData API)
 	private String extractValue(String text, String label) {

 		if (text == null)
 			return null;

 		Pattern pattern = Pattern.compile(label + ":\\s*([^\\n]+)", Pattern.CASE_INSENSITIVE);
 		Matcher matcher = pattern.matcher(text);

 		return matcher.find() ? matcher.group(1).trim() : null;
 	}
}