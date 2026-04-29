package com.clideOffice.clideApp.common.ocr_project.sds.Section9.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.Section9.Service.Section9Service;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section9RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section9ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Section9Controller implements Section9Interface {

    private final Section9Service section9Service;

    // POST
    @Override
    public ResponseEntity<Section9ResponseDTO> addOrUpdate(Section9RequestDTO requestDTO) {
        try {
            Section9ResponseDTO response = section9Service.addOrUpdate(requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error saving Section 9 data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /* ================= GET SECTION 9 ================= */
    @Override
    public ResponseEntity<Section9ResponseDTO> getBySdsId(Long sdsId) {
        try {
            Section9ResponseDTO response = section9Service.getBySdsId(sdsId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching Section 9 for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Section9ResponseDTO> update(Long id, Section9RequestDTO requestDTO) {
        try {
            Section9ResponseDTO response = section9Service.update(id, requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating Section 9 for ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}