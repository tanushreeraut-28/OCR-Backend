package com.clideOffice.clideApp.common.ocr_project.sds.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.CreateVersionRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UpdateSection1RequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.ConfirmOCRResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.CreateVersionResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DashboardSummaryResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DetailsResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.OcrExtractedDataResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.RemoveUserResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetPlantResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetServiceResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UpdateSection1ResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.service.SdsService;

import com.clideOffice.clideApp.common.ocr_project.util.DatabaseContextHolder;

@RestController
@RequestMapping("/api")
public class SdsController implements SdsApi {

	private static final Logger logger = LoggerFactory.getLogger(SdsController.class);

    	private final SdsService sdsService;
	

	public SdsController(
     		SdsService sdsService
			) {
            this.sdsService = sdsService;
	}

	/* ================= SDS Upload API ================= */
	
	@Override
	public ResponseEntity<UploadResponseDto> uploadSds(UploadRequestDto request) {
		try {
			UploadResponseDto response = sdsService.uploadSds(request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error occurred while uploading SDS", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			DatabaseContextHolder.clear();
		}
	}

	/* ================= Details API ================= */
	@Override
	public ResponseEntity<DetailsResponseDto> getSdsDetails(Long sdsId) {
		try {
			DetailsResponseDto response = sdsService.getSdsDetails(sdsId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error occurred while fetching SDS Details", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			DatabaseContextHolder.clear();
		}
	}

	/* ================= Update Section1 API ================= */

	@Override
	public ResponseEntity<UpdateSection1ResponseDto> updateSection1(Long sdsId, UpdateSection1RequestDto request) {
		try {
			UpdateSection1ResponseDto response = sdsService.updateSection1(sdsId, request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error occurred while updating SDS Section1", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			DatabaseContextHolder.clear();
		}
	}

	/* ================= Create Version API ================= */
	@Override
	public ResponseEntity<CreateVersionResponseDto> createVersion(
	        @PathVariable Long sdsId,
	        @ModelAttribute CreateVersionRequestDto request){
		try {
			CreateVersionResponseDto response = sdsService.createVersion(sdsId, request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error occurred while creating SDS Version", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			DatabaseContextHolder.clear();
		}
	}

	/* ================= OCR Extracted Data API ================= */
	@Override
	public ResponseEntity<OcrExtractedDataResponseDto> getOcrExtractedData(@PathVariable Long sdsId) {
		try {
			OcrExtractedDataResponseDto response = sdsService.getOcrExtractedData(sdsId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error occurred while fetching OCR Extracted Data", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			DatabaseContextHolder.clear();
		}
	}

	/* ================= Confirm OCR API ================= */
	@Override
	public ResponseEntity<ConfirmOCRResponseDto> confirmOCR(@PathVariable Long sdsId) {
		try {
			ConfirmOCRResponseDto response = sdsService.confirmOCR(sdsId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error occurred while confirming OCR data", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			DatabaseContextHolder.clear();
		}
	}

	/* ================= Dashboard Summary API ================= */
	@Override
	public ResponseEntity<DashboardSummaryResponseDto> getDashboardSummary() {
		try {
			DashboardSummaryResponseDto response = sdsService.getDashboardSummary();
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error occurred while fetching dashboard summary", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			DatabaseContextHolder.clear();
		}
	}

	/* ================= Get All Services API ================= */

	@Override
	public ResponseEntity<List<GetServiceResponseDto>> getAllServices() {
		try {
			List<GetServiceResponseDto> response = sdsService.getAllServices();
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error occurred while fetching services", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			DatabaseContextHolder.clear();
		}
	}

	/* ================= Get Plants by Service API ================= */

	@Override
	public ResponseEntity<List<GetPlantResponseDto>> getPlantsByService(Long serviceId) {
		try {
			List<GetPlantResponseDto> response = sdsService.getPlantsByService(serviceId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error occurred while fetching plants", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			DatabaseContextHolder.clear();
		}
	}

	/* ================= Remove User API ================= */

	@Override
	public ResponseEntity<RemoveUserResponseDto> removeUser(Integer userId) {
		try {
			RemoveUserResponseDto response = sdsService.removeUser(userId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error occurred while removing user", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			DatabaseContextHolder.clear();
		}
	}
}