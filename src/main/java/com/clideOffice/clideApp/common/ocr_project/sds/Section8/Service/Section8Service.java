package com.clideOffice.clideApp.common.ocr_project.sds.Section8.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.EngineeringControlRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.ExposureLimitRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.EngineeringControlResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.ExposureLimitResponseDTO;

import java.util.List;

public interface Section8Service {

    // POST Exposure Limit
    ExposureLimitResponseDTO addExposureLimit(Long sdsId, ExposureLimitRequestDTO requestDTO);

    // GET Exposure Limits
    List<ExposureLimitResponseDTO> getExposureLimits(Long sdsId);

    // PUT Exposure Limit
    ExposureLimitResponseDTO updateExposureLimit(Long id, ExposureLimitRequestDTO requestDTO);

    // DELETE Exposure Limit
    void deleteExposureLimit(Long id);

    // GET Engineering Controls
    EngineeringControlResponseDTO getEngineeringControl(Long sdsId);

    // UPSERT Engineering Controls
    EngineeringControlResponseDTO upsertEngineeringControl(Long sdsId, EngineeringControlRequestDTO requestDTO);
}