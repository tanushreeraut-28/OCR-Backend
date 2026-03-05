package com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.DetailsProjection;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.DetailsRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DetailsResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section1ResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.service.DetailsService;

@Service
@RequiredArgsConstructor
public class DetailsServiceImpl implements DetailsService {

    private final DetailsRepository detailsRepository;

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
}