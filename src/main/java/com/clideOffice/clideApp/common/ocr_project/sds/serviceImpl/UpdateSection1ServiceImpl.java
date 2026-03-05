package com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clideOffice.clideApp.common.ocr_project.sds.repository.UpdateSection1Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UpdateSection1RequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UpdateSection1ResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.service.UpdateSection1Service;

@Service
public class UpdateSection1ServiceImpl implements UpdateSection1Service {

    private final UpdateSection1Repository repository;

    public UpdateSection1ServiceImpl(UpdateSection1Repository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public UpdateSection1ResponseDto updateSection1(Long sdsId, UpdateSection1RequestDto request) {

        repository.updateSection1(
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
}