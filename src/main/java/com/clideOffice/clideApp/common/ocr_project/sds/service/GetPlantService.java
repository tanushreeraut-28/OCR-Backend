package com.clideOffice.clideApp.common.ocr_project.sds.service;

import java.util.List;

import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetPlantResponseDto;

public interface GetPlantService {

    List<GetPlantResponseDto> getPlantsByService(Long serviceId);

}