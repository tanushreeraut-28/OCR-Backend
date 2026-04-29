package com.clideOffice.clideApp.common.ocr_project.sds.Section16.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.Section16.Service.Section16Service;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section16RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section16ResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.util.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Section16Controller implements Section16Interface {

    private final Section16Service section16Service;

    /* ================= POST ================= */
    @Override
    public ResponseEntity<Section16ResponseDTO> addItem(Section16RequestDTO requestDTO) {
        try {
            return ResponseEntity.ok(section16Service.addItem(requestDTO)); // Save or update
        } catch (Exception e) {
            log.error("Error saving Section16", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= GET ================= */
    @Override
    public ResponseEntity<Section16ResponseDTO> getBySdsId(Long sdsId) {
        try {
            return ResponseEntity.ok(section16Service.getBySdsId(sdsId)); // Fetch
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error fetching Section16 for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= DELETE ================= */
    @Override
    public ResponseEntity<String> deleteBySdsId(Long sdsId) {
        try {
            section16Service.deleteBySdsId(sdsId); // Delete
            return ResponseEntity.ok("Section16 deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error deleting Section16 for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting Section16");
        } finally {
            DatabaseContextHolder.clear();
        }
    }
}