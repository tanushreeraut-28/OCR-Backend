package com.clideOffice.clideApp.common.ocr_project.sds.Section10.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.Section10.Service.Section10Service;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section10RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section10ResponseDTO;
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
