package com.clideOffice.clideApp.common.ocr_project.sds.Section4.ServiceImpl;

import com.clideOffice.clideApp.common.ocr_project.sds.Section4.Service.Section4Service;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section4.FirstAidMeasures;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section4.FirstAidSpecialTreatment;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.FirstAidMeasuresRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.FirstAidSpecialTreatmentRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SpecialTreatmentRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.FirstAidMeasureDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.FirstAidMeasuresResponseDTO;

import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SpecialTreatmentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Section4ServiceImpl implements Section4Service {

    private final FirstAidMeasuresRepository repository;
    private final FirstAidSpecialTreatmentRepository specialTreatmentRepository;

    // GET First Aid Measures
    @Override
    public FirstAidMeasuresResponseDTO getFirstAidMeasures(Long sdsId) {

        List<FirstAidMeasures> dataList =
                repository.findBySdsIdAndIsActiveTrue(sdsId);

        if (dataList.isEmpty()) {
            throw new RuntimeException("First Aid Measures not found for SDS ID: " + sdsId);
        }

        List<FirstAidMeasureDTO> dtoList = dataList.stream()
                .map(data -> FirstAidMeasureDTO.builder()
                        .id(data.getId())
                        .emergencyOverview(data.getEmergencyOverview())
                        .generalAdvice(data.getGeneralAdvice())
                        .afterInhalation(data.getAfterInhalation())
                        .afterSkinContact(data.getAfterSkinContact())
                        .afterEyeContact(data.getAfterEyeContact())
                        .afterSwallowing(data.getAfterSwallowing())
                        .immediateMedicalAttention(data.getImmediateMedicalAttention())
                        .build()
                ).toList();

        return FirstAidMeasuresResponseDTO.builder()
                .sdsId(sdsId)
                .measures(dtoList)
                .build();
    }

    // ADD Special Treatment
    @Override
    public SpecialTreatmentResponseDTO addSpecialTreatment(SpecialTreatmentRequestDTO requestDTO) {

        FirstAidSpecialTreatment entity = FirstAidSpecialTreatment.builder()
                .sdsId(requestDTO.getSdsId())
                .treatment(requestDTO.getTreatment())
                .build();

        FirstAidSpecialTreatment saved = specialTreatmentRepository.save(entity);

        return SpecialTreatmentResponseDTO.builder()
                .id(saved.getId())
                .sdsId(saved.getSdsId())
                .treatment(saved.getTreatment())
                .build();
    }
}