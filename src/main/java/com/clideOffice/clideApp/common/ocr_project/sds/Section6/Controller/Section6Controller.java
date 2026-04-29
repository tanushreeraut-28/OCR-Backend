package com.clideOffice.clideApp.common.ocr_project.sds.Section6.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.Section6.Service.Section6Service;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section6PartialUpdateRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section6PartialUpdateResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section6ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Section6Controller implements Section6Interface {

    private final Section6Service section6Service;

    /* ================= GET SECTION 6 ================= */
    @Override
    public ResponseEntity<Section6ResponseDTO> getBySdsId(@PathVariable Long sdsId) {
        try {
            Section6ResponseDTO response = section6Service.getBySdsId(sdsId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching Section 6 for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /* ================= PARTIAL UPDATE ================= */
    @Override
    public ResponseEntity<Section6PartialUpdateResponseDto> partialUpdate(
            @PathVariable Long id,
            @RequestBody Section6PartialUpdateRequestDTO requestDTO) {
        try {
            requestDTO.setId(id);
            Section6PartialUpdateResponseDto response = section6Service.partialUpdate(requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating Section 6 field: {}", requestDTO.getField(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}