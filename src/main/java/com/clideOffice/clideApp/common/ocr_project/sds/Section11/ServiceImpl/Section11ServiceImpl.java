package com.clideOffice.clideApp.common.ocr_project.sds.Section11.ServiceImpl;

import com.clideOffice.clideApp.common.ocr_project.sds.Section11.Service.Section11Service;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.Section11.Section11;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.Section11Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section11RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section11DeleteResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section11ResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Section11ServiceImpl implements Section11Service {

    private final Section11Repository repository;

    @Override
    public Section11ResponseDTO addOrUpdate(Section11RequestDTO dto) {

        // 🔥 Validate input (important)
        if (dto.getSdsId() == null) {
            throw new IllegalArgumentException("sdsId must not be null");
        }

        // 🔥 UPSERT: find existing or create new
        Section11 entity = repository.findBySdsId(dto.getSdsId())
                .orElse(Section11.builder()
                        .sdsId(dto.getSdsId())
                        .build());

        // 🔹 Update all fields
        entity.setHazardClasses(dto.getHazardClasses());
        entity.setTestSummary(dto.getTestSummary());
        entity.setToxicologicalProperties(dto.getToxicologicalProperties());
        entity.setRoutesOfExposure(dto.getRoutesOfExposure());
        entity.setSymptoms(dto.getSymptoms());
        entity.setDelayedEffects(dto.getDelayedEffects());
        entity.setInteractiveEffects(dto.getInteractiveEffects());
        entity.setAbsenceOfData(dto.getAbsenceOfData());
        entity.setOtherHazards(dto.getOtherHazards());

        // 💾 Save (INSERT or UPDATE)
        Section11 saved = repository.save(entity);

        log.info("Section11 saved successfully for sdsId={}", saved.getSdsId());

        // 🔹 Convert to Response
        return Section11ResponseDTO.builder()
                .sdsId(saved.getSdsId())
                .hazardClasses(saved.getHazardClasses())
                .testSummary(saved.getTestSummary())
                .toxicologicalProperties(saved.getToxicologicalProperties())
                .routesOfExposure(saved.getRoutesOfExposure())
                .symptoms(saved.getSymptoms())
                .delayedEffects(saved.getDelayedEffects())
                .interactiveEffects(saved.getInteractiveEffects())
                .absenceOfData(saved.getAbsenceOfData())
                .otherHazards(saved.getOtherHazards())
                .build();
    }

    // GET
    @Override
    public Section11ResponseDTO getBySdsId(Long sdsId) {

        Section11 entity = repository.findBySdsId(sdsId)
                .orElseThrow(() -> new RuntimeException("Section11 not found with sdsId: " + sdsId));

        return Section11ResponseDTO.builder()
                .sdsId(entity.getSdsId())
                .hazardClasses(entity.getHazardClasses())
                .testSummary(entity.getTestSummary())
                .toxicologicalProperties(entity.getToxicologicalProperties())
                .routesOfExposure(entity.getRoutesOfExposure())
                .symptoms(entity.getSymptoms())
                .delayedEffects(entity.getDelayedEffects())
                .interactiveEffects(entity.getInteractiveEffects())
                .absenceOfData(entity.getAbsenceOfData())
                .otherHazards(entity.getOtherHazards())
                .build();
    }

    // PATCH
    @Override
    public Section11ResponseDTO partialUpdate(Long sdsId, Section11RequestDTO dto) {

        Section11 entity = repository.findBySdsId(sdsId)
                .orElseThrow(() -> new RuntimeException("Section11 not found with sdsId: " + sdsId));

        // 🔥 Only update NON-NULL fields

        if (dto.getHazardClasses() != null) {
            entity.setHazardClasses(dto.getHazardClasses());
        }

        if (dto.getTestSummary() != null) {
            entity.setTestSummary(dto.getTestSummary());
        }

        if (dto.getToxicologicalProperties() != null) {
            entity.setToxicologicalProperties(dto.getToxicologicalProperties());
        }

        if (dto.getRoutesOfExposure() != null) {
            entity.setRoutesOfExposure(dto.getRoutesOfExposure());
        }

        if (dto.getSymptoms() != null) {
            entity.setSymptoms(dto.getSymptoms());
        }

        if (dto.getDelayedEffects() != null) {
            entity.setDelayedEffects(dto.getDelayedEffects());
        }

        if (dto.getInteractiveEffects() != null) {
            entity.setInteractiveEffects(dto.getInteractiveEffects());
        }

        if (dto.getAbsenceOfData() != null) {
            entity.setAbsenceOfData(dto.getAbsenceOfData());
        }

        if (dto.getOtherHazards() != null) {
            entity.setOtherHazards(dto.getOtherHazards());
        }

        Section11 updated = repository.save(entity);

        return Section11ResponseDTO.builder()
                .sdsId(updated.getSdsId())
                .hazardClasses(updated.getHazardClasses())
                .testSummary(updated.getTestSummary())
                .toxicologicalProperties(updated.getToxicologicalProperties())
                .routesOfExposure(updated.getRoutesOfExposure())
                .symptoms(updated.getSymptoms())
                .delayedEffects(updated.getDelayedEffects())
                .interactiveEffects(updated.getInteractiveEffects())
                .absenceOfData(updated.getAbsenceOfData())
                .otherHazards(updated.getOtherHazards())
                .build();
    }

    // DELETE
    @Override
    public Section11DeleteResponseDTO delete(Long sdsId) {

        Section11 entity = repository.findBySdsId(sdsId)
                .orElseThrow(() -> new RuntimeException("Section11 not found with sdsId: " + sdsId));

        repository.delete(entity);

        return Section11DeleteResponseDTO.builder()
                .message("Section11 deleted successfully")
                .sdsId(sdsId)
                .build();
    }

}