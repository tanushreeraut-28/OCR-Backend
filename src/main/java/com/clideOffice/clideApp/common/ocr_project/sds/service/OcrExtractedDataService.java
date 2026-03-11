package com.clideOffice.clideApp.common.ocr_project.sds.service;

import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.OcrExtractedDataResponseDto;

public interface OcrExtractedDataService {

    OcrExtractedDataResponseDto getOcrExtractedData(Long sdsId); 

}