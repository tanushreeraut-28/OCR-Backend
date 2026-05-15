package com.clideOffice.clideApp.common.ocr_project.sds.upload.service;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadSdsResponseDto;

public interface UploadService {

    // SDS Upload API 1 & 2
    UploadSdsResponseDto uploadSdsFile(UploadRequestDto request);

    // SDS Upload API 5 & 6
    UploadSdsResponseDto uploadSdsSection5And6(UploadRequestDto request);

    // SDS Upload API 9 & 10 & 11
    UploadSdsResponseDto uploadSection9And10And11(UploadRequestDto request);

}
