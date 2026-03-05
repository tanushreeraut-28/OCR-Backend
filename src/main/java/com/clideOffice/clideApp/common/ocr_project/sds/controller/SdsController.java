package com.clideOffice.clideApp.common.ocr_project.sds.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.CreateVersionRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.DetailsRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UpdateSection1RequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.CreateVersionResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DetailsResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UpdateSection1ResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl.CreateVersionServiceImpl;
import com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl.DetailsServiceImpl;
import com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl.UploadServiceImpl;
import com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl.UpdateSection1ServiceImpl;
import com.clideOffice.clideApp.common.ocr_project.util.DatabaseContextHolder;

@RestController
@RequestMapping("/api")
public class SdsController implements SdsApi {

    private static final Logger logger =
            LoggerFactory.getLogger(SdsController.class);

//    private final UploadServiceImpl uploadServiceImpl;
    private final DetailsServiceImpl detailsServiceImpl;
    private final UpdateSection1ServiceImpl updateSection1ServiceImpl;
    private final CreateVersionServiceImpl createVersionServiceImpl;

    public SdsController(
//    		             UploadServiceImpl uploadServiceImpl,
    		             DetailsServiceImpl detailsServiceImpl,
    		             UpdateSection1ServiceImpl updateSection1ServiceImpl,
    		             CreateVersionServiceImpl createVersionServiceImpl) {
//        this.uploadServiceImpl = uploadServiceImpl;
        this.detailsServiceImpl = detailsServiceImpl;
        this.updateSection1ServiceImpl = updateSection1ServiceImpl;
        this.createVersionServiceImpl = createVersionServiceImpl;
    }

//    /* ================= SDS Upload API ================= */
//    @Override
//    public ResponseEntity<UploadResponseDto> uploadSds(UploadRequestDto request) {
//        try {
//            UploadResponseDto response = uploadServiceImpl.uploadSds(request);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            logger.error("Error occurred while uploading SDS", e);
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .build();
//        } finally {
//            DatabaseContextHolder.clear();
//        }
//    }
    
    /* ================= Details API ================= */
    @Override
    public ResponseEntity<DetailsResponseDto> getSdsDetails(Long sdsId) {
        try {
            DetailsResponseDto response = detailsServiceImpl.getSdsDetails(sdsId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
        	logger.error("Error occurred while fetching SDS Details", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }
    
    /* ================= Update Section1 API ================= */

    @Override
    public ResponseEntity<UpdateSection1ResponseDto> updateSection1(Long sdsId,UpdateSection1RequestDto request) {
        try {
        	UpdateSection1ResponseDto response = updateSection1ServiceImpl.updateSection1(sdsId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error occurred while updating SDS Section1", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }
    
    /* ================= Create Version API ================= */
    @Override
    public ResponseEntity<CreateVersionResponseDto> createVersion(Long sdsId, CreateVersionRequestDto request) {
        try {
        	CreateVersionResponseDto response = createVersionServiceImpl.createVersion(sdsId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error occurred while creating SDS Version", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }
    
}