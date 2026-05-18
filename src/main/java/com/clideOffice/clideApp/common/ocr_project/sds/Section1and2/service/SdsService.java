package com.clideOffice.clideApp.common.ocr_project.sds.Section1and2.service;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.CreateVersionRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.HandleHazardOcrRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UpdateSection1RequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.ConfirmOCRResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.CreateVersionResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DashboardSummaryResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DeleteHazardChildResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DetailsResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetPlantResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetServiceResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.HandleHazardOcrResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.HazardMasterResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.OcrExtractedDataResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.OcrExtractedDataSection2ResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.RemoveUserResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UpdateSection1ResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadResponseDto;

//@RestController
public interface SdsService {

//	UploadResponseDto uploadSds(UploadRequestDto request);
	
	UpdateSection1ResponseDto updateSection1(Long sdsId, UpdateSection1RequestDto request);
	
	RemoveUserResponseDto removeUser(Integer userId);
	
	OcrExtractedDataResponseDto getOcrExtractedData(Long sdsId); 
	
	List<GetServiceResponseDto> getAllServices();
	
	List<GetPlantResponseDto> getPlantsByService(Long serviceId);
	
	DetailsResponseDto getSdsDetails(Long sdsId);
	
	DashboardSummaryResponseDto getDashboardSummary();
	 
	CreateVersionResponseDto createVersion(Long sdsId, CreateVersionRequestDto requestDto);
	
	ConfirmOCRResponseDto confirmOCR(Long sdsId);
	
	// Section 2 Hazard Child Management
	DeleteHazardChildResponseDto deleteHazardChild(String type, Long id);
	
	// Section 2 Hazard OCR Handling
	HandleHazardOcrResponseDto handleHazardOcr(Long sdsId,HandleHazardOcrRequestDto request);
	
	// Section 2 Master Hazard Data API 
	HazardMasterResponseDTO getHazardMasterData();

	// Section 2 OCR Extracted Data API
	OcrExtractedDataSection2ResponseDto getOcrExtractedDataSection2(Long sdsId);
	
}
