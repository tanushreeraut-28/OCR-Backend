package com.clideOffice.clideApp.common.ocr_project.sds.service;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.CreateVersionRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.CreateVersionResponseDto;

public interface CreateVersionService {

    CreateVersionResponseDto createVersion(Long sdsId, CreateVersionRequestDto requestDto);
}