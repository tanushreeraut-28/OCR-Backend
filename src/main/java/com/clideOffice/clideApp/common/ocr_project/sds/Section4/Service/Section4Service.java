package com.clideOffice.clideApp.common.ocr_project.sds.Section4.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SpecialTreatmentRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.FirstAidMeasuresResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SpecialTreatmentResponseDTO;

public interface Section4Service {

    // GET First Aid Measures
    FirstAidMeasuresResponseDTO getFirstAidMeasures(Long sdsId);

    // ADD Special Treatment
    SpecialTreatmentResponseDTO addSpecialTreatment(SpecialTreatmentRequestDTO requestDTO);
}