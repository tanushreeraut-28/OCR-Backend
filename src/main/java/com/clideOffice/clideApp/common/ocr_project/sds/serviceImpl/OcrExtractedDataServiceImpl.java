package com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl;

import org.springframework.stereotype.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.OcrExtractedDataProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.OcrExtractedDataRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.OcrExtractedDataResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.service.OcrExtractedDataService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OcrExtractedDataServiceImpl implements OcrExtractedDataService {

    private final OcrExtractedDataRepository repository;

    @Override
    public OcrExtractedDataResponseDto getOcrExtractedData(Long sdsId) {

        // fetch OCR extracted data using projection
        OcrExtractedDataProjection data = repository.getOcrExtractedData(sdsId);

        if (data == null) {
            return OcrExtractedDataResponseDto.builder()
                    .message("OCR data not found")
                    .build();
        }

        // map projection → response dto
        return OcrExtractedDataResponseDto.builder()
                .sdsId(data.getSdsId())
                .versionId(data.getVersionId())
                .productIdentifier(data.getProductIdentifier())
                .otherIdentification(data.getOtherIdentification())
                .sdsNumber(data.getSdsNumber())
                .recommendedUse(data.getRecommendedUse())
                .recommendedRestrictions(data.getRecommendedRestrictions())
                .manufacturerInfo(data.getManufacturerInfo())
                .sourceType(data.getSourceType())
                .rawText(data.getRawText())
                .confidenceScore(data.getConfidenceScore())
                .message("OCR extracted data fetched successfully")
                .build();
    }
}