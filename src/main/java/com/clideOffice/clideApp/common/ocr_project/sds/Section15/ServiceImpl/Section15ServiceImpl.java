package com.clideOffice.clideApp.common.ocr_project.sds.Section15.ServiceImpl;

import com.clideOffice.clideApp.common.ocr_project.sds.Section15.Service.Section15Service;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section15.RegulatoryInformation;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.Section15Projection;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.Section15Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section15RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section15ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class Section15ServiceImpl implements Section15Service {

    private final Section15Repository repository;

    /* ================= UPSERT ================= */
    @Override
    @Transactional
    public Section15ResponseDTO addItem(Section15RequestDTO dto) {

        RegulatoryInformation entity = repository.findEntityBySdsId(dto.getSdsId())
                .orElse(RegulatoryInformation.builder()
                        .sdsId(dto.getSdsId())
                        .build());

        // 🔹 Set all fields
        entity.setSafetyHealthEnvironmentalRegulations(dto.getSafetyHealthEnvironmentalRegulations());
        entity.setDirective2012_18_EU(dto.getDirective2012_18_EU());
        entity.setReachAnnexXVII(dto.getReachAnnexXVII());
        entity.setDirective2011_65_EU(dto.getDirective2011_65_EU());
        entity.setRegulationEU2019_1148(dto.getRegulationEU2019_1148());
        entity.setRegulationEC273_2004(dto.getRegulationEC273_2004());
        entity.setRegulationEC111_2005(dto.getRegulationEC111_2005());
        entity.setChemicalSafetyAssessment(dto.getChemicalSafetyAssessment());

        RegulatoryInformation saved = repository.save(entity);

        return mapToResponse(saved);
    }

    /* ================= GET ================= */
    @Override
    public Section15ResponseDTO getBySdsId(Long sdsId) {

        Section15Projection p = repository.findBySdsId(sdsId)
                .orElseThrow(() -> new RuntimeException("Section15 data not found for sdsId: " + sdsId));

        return Section15ResponseDTO.builder()
                .id(p.getId())
                .sdsId(p.getSdsId())
                .safetyHealthEnvironmentalRegulations(p.getSafetyHealthEnvironmentalRegulations())
                .directive2012_18_EU(p.getDirective2012_18_EU())
                .reachAnnexXVII(p.getReachAnnexXVII())
                .directive2011_65_EU(p.getDirective2011_65_EU())
                .regulationEU2019_1148(p.getRegulationEU2019_1148())
                .regulationEC273_2004(p.getRegulationEC273_2004())
                .regulationEC111_2005(p.getRegulationEC111_2005())
                .chemicalSafetyAssessment(p.getChemicalSafetyAssessment())
                .build();
    }

    /* ================= DELETE ================= */
    @Override
    @Transactional
    public void deleteBySdsId(Long sdsId) {

        // 🔹 Optional check (recommended)
        repository.findBySdsId(sdsId)
                .orElseThrow(() -> new RuntimeException("Section15 not found for deletion"));

        repository.deleteBySdsId(sdsId);
    }

    /* ================= MAPPER ================= */
    private Section15ResponseDTO mapToResponse(RegulatoryInformation e) {

        return Section15ResponseDTO.builder()
                .id(e.getId())
                .sdsId(e.getSdsId())
                .safetyHealthEnvironmentalRegulations(e.getSafetyHealthEnvironmentalRegulations())
                .directive2012_18_EU(e.getDirective2012_18_EU())
                .reachAnnexXVII(e.getReachAnnexXVII())
                .directive2011_65_EU(e.getDirective2011_65_EU())
                .regulationEU2019_1148(e.getRegulationEU2019_1148())
                .regulationEC273_2004(e.getRegulationEC273_2004())
                .regulationEC111_2005(e.getRegulationEC111_2005())
                .chemicalSafetyAssessment(e.getChemicalSafetyAssessment())
                .build();
    }
}