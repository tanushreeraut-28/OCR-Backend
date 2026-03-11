package com.clideOffice.clideApp.common.ocr_project.sds.service;

import java.util.List;

import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetServiceResponseDto;

public interface GetServiceService {

    List<GetServiceResponseDto> getAllServices();

}