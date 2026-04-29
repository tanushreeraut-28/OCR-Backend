package com.clideOffice.clideApp.common.ocr_project.sds.Section11.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.Section11.Service.Section11Service;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section11RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section11DeleteResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section11ResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.util.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Section11Controller implements Section11Interface{

    private final Section11Service section11Service;

    /* ================= POST ================= */
    @Override
    public ResponseEntity<Section11ResponseDTO> addItem(Section11RequestDTO requestDTO) {

        try {
            return ResponseEntity.ok(section11Service.addOrUpdate(requestDTO));

        } catch (Exception e) {
            log.error("Error adding/updating Section11", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= GET ================= */
    @Override
    public ResponseEntity<Section11ResponseDTO> getBySdsId(Long sdsId) {

        try {
            return ResponseEntity.ok(section11Service.getBySdsId(sdsId));

        } catch (RuntimeException e) {
            log.error("Section11 not found for sdsId={}", sdsId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception e) {
            log.error("Error fetching Section11", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= PATCH ================= */
    @Override
    public ResponseEntity<Section11ResponseDTO> partialUpdate(Long sdsId,
                                                              Section11RequestDTO requestDTO) {
        try {
            return ResponseEntity.ok(section11Service.partialUpdate(sdsId, requestDTO));

        } catch (RuntimeException e) {
            log.error("Section11 not found for sdsId={}", sdsId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception e) {
            log.error("Error updating Section11", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            DatabaseContextHolder.clear();
        }
    }


    /* ================= DELETE ================= */
    @Override
    public ResponseEntity<Section11DeleteResponseDTO> delete(Long sdsId) {

        try {
            return ResponseEntity.ok(section11Service.delete(sdsId));

        } catch (RuntimeException e) {
            log.error("Section11 not found for delete sdsId={}", sdsId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception e) {
            log.error("Error deleting Section11", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            DatabaseContextHolder.clear();
        }
    }

}
