package com.clideOffice.clideApp.common.ocr_project.sds.service;

import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.ConfirmOCRResponseDto;

public interface ConfirmOCRService {
	
	ConfirmOCRResponseDto confirmOCR(Long sdsId);

}
