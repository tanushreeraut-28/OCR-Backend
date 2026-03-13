package com.clideOffice.clideApp.common.ocr_project.sds.service;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.CreateVersionRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UpdateSection1RequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.ConfirmOCRResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.CreateVersionResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DashboardSummaryResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DetailsResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetPlantResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetServiceResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.OcrExtractedDataResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.RemoveUserResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UpdateSection1ResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadResponseDto;

@RestController
public interface SdsService {

	UploadResponseDto uploadSds(UploadRequestDto request);
	
	UpdateSection1ResponseDto updateSection1(Long sdsId, UpdateSection1RequestDto request);
	
	RemoveUserResponseDto removeUser(Integer userId);
	
	OcrExtractedDataResponseDto getOcrExtractedData(Long sdsId); 
	
	List<GetServiceResponseDto> getAllServices();
	
	List<GetPlantResponseDto> getPlantsByService(Long serviceId);
	
	DetailsResponseDto getSdsDetails(Long sdsId);
	
	DashboardSummaryResponseDto getDashboardSummary();
	 
	CreateVersionResponseDto createVersion(Long sdsId, CreateVersionRequestDto requestDto);
	
	ConfirmOCRResponseDto confirmOCR(Long sdsId);

	
}
