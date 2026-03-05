package com.clideOffice.clideApp.common.ocr_project.sds.service;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadResponseDto;

public interface UploadService {

    UploadResponseDto uploadSds(UploadRequestDto request);

}