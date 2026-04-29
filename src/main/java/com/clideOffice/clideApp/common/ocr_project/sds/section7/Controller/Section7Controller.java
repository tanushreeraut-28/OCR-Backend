package com.clideOffice.clideApp.common.ocr_project.sds.section7.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SafeHandlingRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SafeStorageRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SpecificEndUseRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SafeHandlingResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SafeStorageResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SpecificEndUseResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.section7.Service.Section7Service;
import com.clideOffice.clideApp.common.ocr_project.util.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Section7Controller implements Section7Interface {

    private final Section7Service section7Service;

    /* ================= GET Safe Handling ================= */
    @Override
    public ResponseEntity<SafeHandlingResponseDTO> getSafeHandling(@PathVariable Long sdsId) {
        try {
            SafeHandlingResponseDTO response = section7Service.getSafeHandling(sdsId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching Safe Handling for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= ADD Safe Handling ================= */
    @Override
    public ResponseEntity<SafeHandlingResponseDTO> addSafeHandling(@RequestBody SafeHandlingRequestDTO requestDTO) {
        try {
            SafeHandlingResponseDTO response = section7Service.addSafeHandling(requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error adding Safe Handling for SDS ID: {}", requestDTO.getSdsId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= UPDATE Safe Handling ================= */
    @Override
    public ResponseEntity<SafeHandlingResponseDTO> updateSafeHandling(@PathVariable Long id, @RequestBody SafeHandlingRequestDTO requestDTO) {
        try {
            SafeHandlingResponseDTO response = section7Service.updateSafeHandling(id, requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating Safe Handling ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= GET Safe Storage  ================= */
    @Override
    public ResponseEntity<SafeStorageResponseDTO> getStorageConditions(Long sdsId) {
        try {
            SafeStorageResponseDTO response = section7Service.getStorageConditions(sdsId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching Storage Conditions for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= ADD / UPDATE Safe Storage  ================= */
    @Override
    public ResponseEntity<SafeStorageResponseDTO> addOrUpdate(SafeStorageRequestDTO requestDTO) {
        try {
            SafeStorageResponseDTO response = section7Service.addOrUpdate(requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error saving Storage Conditions for SDS ID: {}", requestDTO.getSdsId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= GET Specific End Use ================= */
    @Override
    public ResponseEntity<SpecificEndUseResponseDTO> getSpecificEndUse(@PathVariable Long sdsId) {
        try {
            return ResponseEntity.ok(section7Service.getSpecificEndUse(sdsId));
        } catch (Exception e) {
            log.error("Error fetching Specific End Use for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= Edit Specific End Use ================= */
    @Override
    public ResponseEntity<SpecificEndUseResponseDTO> addOrUpdateSpecificEndUse(
            @PathVariable Long sdsId,
            @RequestBody SpecificEndUseRequestDTO requestDTO) {

        try {
            return ResponseEntity.ok(section7Service.addOrUpdateSpecificEndUse(sdsId, requestDTO));
        } catch (Exception e) {
            log.error("Error saving Specific End Use for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }
}