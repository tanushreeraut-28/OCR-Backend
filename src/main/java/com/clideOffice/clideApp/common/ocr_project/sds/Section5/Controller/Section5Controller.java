package com.clideOffice.clideApp.common.ocr_project.sds.Section5.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.Section4.Service.Section4Service;
import com.clideOffice.clideApp.common.ocr_project.sds.Section5.Service.Section5Service;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section5FirefightingRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section5FirefightingResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.util.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Section5Controller implements Section5Interface{

    private final Section5Service section5Service;

    /* ================= SAVE / UPDATE FIREFIGHTING ================= */
    @Override
    public ResponseEntity<Section5FirefightingResponseDTO> saveOrUpdateFirefighting(
            Section5FirefightingRequestDTO requestDTO) {

        try {
            Section5FirefightingResponseDTO response =
                    section5Service.saveOrUpdateFirefighting(requestDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error saving/updating Firefighting data for SDS ID: {}",
                    requestDTO.getSdsId(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= GET FIREFIGHTING BY SDS ID ================= */
    @Override
    public ResponseEntity<Section5FirefightingResponseDTO> getFirefightingBySdsId(Long sdsId) {
        try {
            Section5FirefightingResponseDTO response =
                    section5Service.getFirefightingBySdsId(sdsId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error fetching Firefighting data for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            DatabaseContextHolder.clear();
        }
    }

}
