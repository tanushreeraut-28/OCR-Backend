package com.clideOffice.clideApp.common.ocr_project.sds.Section15.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.Section15.Service.Section15Service;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section15RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section15ResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadSdsResponseDto;
import com.clideOffice.clideApp.common.ocr_project.util.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Section15Controller implements Section15Interface {

    private final Section15Service section15Service;

    /* ================= SDS Upload API 9 & 10 & 11 ================= */
    @Override
    public ResponseEntity<UploadSdsResponseDto> uploadSection15And16(UploadRequestDto request) {
        try {
            UploadSdsResponseDto response = section15Service.uploadSection15And16(request);
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

    /* ================= POST ================= */
    @Override
    public ResponseEntity<Section15ResponseDTO> addItem(Section15RequestDTO requestDTO) {

        try {
            return ResponseEntity.ok(section15Service.addItem(requestDTO));

        } catch (Exception e) {
            log.error("Error adding Section15 data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= GET ================= */
    @Override
    public ResponseEntity<Section15ResponseDTO> getBySdsId(Long sdsId) {

        try {
            return ResponseEntity.ok(section15Service.getBySdsId(sdsId));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception e) {
            log.error("Error fetching Section15 for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= DELETE ================= */
    @Override
    public ResponseEntity<String> deleteItem(Long id) {

        try {
            section15Service.deleteBySdsId(id);
            return ResponseEntity.ok("Section15 deleted successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            log.error("Error deleting Section15 ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting Section15");

        } finally {
            DatabaseContextHolder.clear();
        }
    }
}