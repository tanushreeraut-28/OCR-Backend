package com.clideOffice.clideApp.common.ocr_project.sds.Section8.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.Section8.Service.Section8Service;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.EngineeringControlRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.ExposureLimitRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.EngineeringControlResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.ExposureLimitResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.util.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Section8Controller implements Section8Interface {

    private final Section8Service section8Service;

    /* ================= POST ================= */
    @Override
    public ResponseEntity<ExposureLimitResponseDTO> addExposureLimit(
            Long sdsId,
            ExposureLimitRequestDTO requestDTO) {

        try {
            ExposureLimitResponseDTO response =
                    section8Service.addExposureLimit(sdsId, requestDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error adding Exposure Limit for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= GET ================= */
    @Override
    public ResponseEntity<List<ExposureLimitResponseDTO>> getExposureLimits(Long sdsId) {

        try {
            List<ExposureLimitResponseDTO> response =
                    section8Service.getExposureLimits(sdsId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error fetching Exposure Limits for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= UPDATE ================= */
    @Override
    public ResponseEntity<ExposureLimitResponseDTO> updateExposureLimit(
            Long id,
            ExposureLimitRequestDTO requestDTO) {

        try {
            ExposureLimitResponseDTO response =
                    section8Service.updateExposureLimit(id, requestDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error updating Exposure Limit ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= DELETE Exposure Limit ================= */
    @Override
    public ResponseEntity<String> deleteExposureLimit(Long id) {

        try {
            section8Service.deleteExposureLimit(id);
            return ResponseEntity.ok("Exposure Limit deleted successfully");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            log.error("Error deleting Exposure Limit ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting Exposure Limit");

        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= GET Engineering Control================= */
    @Override
    public ResponseEntity<EngineeringControlResponseDTO> getEngineeringControl(Long sdsId) {

        try {
            return ResponseEntity.ok(section8Service.getEngineeringControl(sdsId));

        } catch (Exception e) {
            log.error("Error fetching Engineering Control for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= UPSERT Engineering Control================= */
    @Override
    public ResponseEntity<EngineeringControlResponseDTO> upsertEngineeringControl(
            Long sdsId,
            EngineeringControlRequestDTO requestDTO) {

        try {
            return ResponseEntity.ok(
                    section8Service.upsertEngineeringControl(sdsId, requestDTO)
            );

        } catch (Exception e) {
            log.error("Error saving Engineering Control for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            DatabaseContextHolder.clear();
        }
    }
}