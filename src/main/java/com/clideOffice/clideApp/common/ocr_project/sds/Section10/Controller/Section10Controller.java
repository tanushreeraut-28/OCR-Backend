package com.clideOffice.clideApp.common.ocr_project.sds.Section10.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.Section10.Service.Section10Service;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section10RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section10ResponseDTO;
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
public class Section10Controller implements Section10Interface{

    private final Section10Service section10Service;

    /* ================= SDS Upload API 9 & 10 & 11 ================= */
    @Override
    public ResponseEntity<UploadSdsResponseDto> uploadSection12And13And14(UploadRequestDto request) {
        try {
            UploadSdsResponseDto response = section10Service.uploadSection12And13And14(request);
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
    public ResponseEntity<Section10ResponseDTO> addItem(Section10RequestDTO requestDTO) {

        try {
            return ResponseEntity.ok(section10Service.addItem(requestDTO));

        } catch (Exception e) {
            log.error("Error adding Section10 item", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= DELETE ================= */
    @Override
    public ResponseEntity<String> deleteItem(Long id) {

        try {
            section10Service.deleteItem(id);
            return ResponseEntity.ok("Section10 item deleted successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            log.error("Error deleting Section10 item ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting Section10 item");

        } finally {
            DatabaseContextHolder.clear();
        }
    }
}
