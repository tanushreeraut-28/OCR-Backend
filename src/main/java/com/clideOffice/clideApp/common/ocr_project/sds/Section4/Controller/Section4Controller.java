package com.clideOffice.clideApp.common.ocr_project.sds.Section4.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.Section3.controller.SdsInterface;
import com.clideOffice.clideApp.common.ocr_project.sds.Section3.service.Section3Service;
import com.clideOffice.clideApp.common.ocr_project.sds.Section4.Service.Section4Service;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SpecialTreatmentRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.FirstAidMeasuresResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SpecialTreatmentResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.util.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class Section4Controller implements Section4Interface {

    private final Section4Service section4Service;

    /* ================= GET First Aid Measures ================= */
    @Override
    public ResponseEntity<FirstAidMeasuresResponseDTO> getFirstAidMeasures(@PathVariable Long sdsId) {
        try {
            FirstAidMeasuresResponseDTO response = section4Service.getFirstAidMeasures(sdsId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching First Aid Measures for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= ADD Special Treatment ================= */
    @Override
    public ResponseEntity<SpecialTreatmentResponseDTO> addSpecialTreatment(@RequestBody SpecialTreatmentRequestDTO requestDTO) {
        try {
            SpecialTreatmentResponseDTO response = section4Service.addSpecialTreatment(requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error adding Special Treatment for SDS ID: {}", requestDTO.getSdsId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }
}