package com.clideOffice.clideApp.common.ocr_project.sds.upload.controller;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadSdsResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.upload.service.UploadService;
import com.clideOffice.clideApp.common.ocr_project.util.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UploadController implements UploadInterface {

    private final UploadService uploadService;

    /* ================= SDS Upload API ================= */
    @Override
    public ResponseEntity<UploadSdsResponseDto> uploadSds(UploadRequestDto request) {
        try {
            UploadSdsResponseDto response = uploadService.uploadSdsFile(request);
            if ("DUPLICATE".equalsIgnoreCase(response.getStatus())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(response);
            }
            if ("OCR_FAILED".equalsIgnoreCase(response.getStatus())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }
            if ("SECTION_FAILED".equalsIgnoreCase(response.getStatus())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(response);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Upload failed", e);
            UploadSdsResponseDto errorResponse = new UploadSdsResponseDto();
            errorResponse.setStatus("FAILED");
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= SDS Upload API 5 & 6 ================= */
    @Override
    public ResponseEntity<UploadSdsResponseDto> uploadSdsSection5And6(UploadRequestDto request) {
        try {
            UploadSdsResponseDto response = uploadService.uploadSdsSection5And6(request);
            if ("DUPLICATE".equalsIgnoreCase(response.getStatus())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(response);
            }
            if ("OCR_FAILED".equalsIgnoreCase(response.getStatus())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }
            if ("SECTION_FAILED".equalsIgnoreCase(response.getStatus())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(response);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Upload failed", e);
            UploadSdsResponseDto errorResponse = new UploadSdsResponseDto();
            errorResponse.setStatus("FAILED");
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= SDS Upload API 9 & 10 & 11 ================= */
    @Override
    public ResponseEntity<UploadSdsResponseDto> uploadSection9And10And11(UploadRequestDto request) {
        try {
            UploadSdsResponseDto response = uploadService.uploadSection9And10And11(request);
            if ("DUPLICATE".equalsIgnoreCase(
                    response.getStatus()
            )) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(response);
            }
            if ("OCR_FAILED".equalsIgnoreCase(
                    response.getStatus()
            )) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }
            if ("FAILED".equalsIgnoreCase(
                    response.getStatus()
            )) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(response);
            }
            return ResponseEntity.ok(response);
        } catch (Exception exception) {
            log.error("Upload failed", exception);
            UploadSdsResponseDto response = new UploadSdsResponseDto();
            response.setStatus("FAILED");
            response.setMessage(exception.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        } finally {
            DatabaseContextHolder.clear();
        }
    }
}