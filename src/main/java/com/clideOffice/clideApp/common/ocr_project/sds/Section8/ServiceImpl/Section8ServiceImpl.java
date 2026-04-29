package com.clideOffice.clideApp.common.ocr_project.sds.Section8.ServiceImpl;

import com.clideOffice.clideApp.common.ocr_project.sds.Section8.Service.Section8Service;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section8.EngineeringControl;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section8.ExposureLimit;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section8.Section8;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.EngineeringControlRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.ExposureLimitRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.Section8Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.EngineeringControlRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.ExposureLimitRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.EngineeringControlResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.ExposureLimitResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Section8ServiceImpl implements Section8Service {

    private final Section8Repository section8Repository;
    private final ExposureLimitRepository exposureLimitRepository;
    private final EngineeringControlRepository engineeringControlRepository;

    // POST Exposure Limit
    @Override
    public ExposureLimitResponseDTO addExposureLimit(Long sdsId, ExposureLimitRequestDTO requestDTO) {

        if (sdsId == null) {
            throw new RuntimeException("SDS ID cannot be null");
        }

        // 1. Get or Create Section8
        Section8 section8 = section8Repository.findBySdsId(sdsId)
                .orElseGet(() -> {
                    Section8 newSection = new Section8();
                    newSection.setSdsId(sdsId);
                    return section8Repository.save(newSection);
                });

        // 2. Create Entity
        ExposureLimit entity = new ExposureLimit();
        entity.setCasNo(requestDTO.getCasNo());
        entity.setChemicalName(requestDTO.getChemicalName());
        entity.setCountryOrganization(requestDTO.getCountryOrganization());
        entity.setLimitType(requestDTO.getLimitType());
        entity.setValueNotes(requestDTO.getValueNotes());
        entity.setUnit(requestDTO.getUnit());
        entity.setSection8(section8);

        // 3. Save
        ExposureLimit saved = exposureLimitRepository.save(entity);

        // 4. Return Response
        return mapToResponse(saved, section8.getSdsId());
    }

    // POST Mappper
    private ExposureLimitResponseDTO mapToResponse(ExposureLimit entity, Long sdsId) {
        return ExposureLimitResponseDTO.builder()
                .id(entity.getId())
                .sdsId(sdsId)
                .casNo(entity.getCasNo())
                .chemicalName(entity.getChemicalName())
                .countryOrganization(entity.getCountryOrganization())
                .limitType(entity.getLimitType())
                .valueNotes(entity.getValueNotes())
                .unit(entity.getUnit())
                .build();
    }

    // GET Exposure Limits
    @Override
    public List<ExposureLimitResponseDTO> getExposureLimits(Long sdsId) {

        Section8 section8 = section8Repository.findBySdsId(sdsId)
                .orElseThrow(() -> new RuntimeException("Section8 not found for SDS ID: " + sdsId));

        return section8.getExposureLimits()
                .stream()
                .map(entity -> mapToResponse(entity, sdsId))
                .toList();
    }

    // PUT Exposure Limit
    @Override
    public ExposureLimitResponseDTO updateExposureLimit(Long id, ExposureLimitRequestDTO requestDTO) {

        // 1. Fetch existing record
        ExposureLimit entity = exposureLimitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExposureLimit not found with id: " + id));

        // 2. Update fields
        entity.setCasNo(requestDTO.getCasNo());
        entity.setChemicalName(requestDTO.getChemicalName());
        entity.setCountryOrganization(requestDTO.getCountryOrganization());
        entity.setLimitType(requestDTO.getLimitType());
        entity.setValueNotes(requestDTO.getValueNotes());
        entity.setUnit(requestDTO.getUnit());

        // 3. Save
        ExposureLimit updated = exposureLimitRepository.save(entity);

        // 4. Return DTO
        return ExposureLimitResponseDTO.builder()
                .id(updated.getId())
                .sdsId(updated.getSection8().getSdsId())
                .casNo(updated.getCasNo())
                .chemicalName(updated.getChemicalName())
                .countryOrganization(updated.getCountryOrganization())
                .limitType(updated.getLimitType())
                .valueNotes(updated.getValueNotes())
                .unit(updated.getUnit())
                .build();
    }

    // DELETE Exposure Limit
    @Override
    public void deleteExposureLimit(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("ExposureLimit ID cannot be null");
        }

        ExposureLimit entity = exposureLimitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExposureLimit not found with id: " + id));

        exposureLimitRepository.delete(entity);
    }

    /* ================= GET Engineering Controls ================= */
    @Override
    public EngineeringControlResponseDTO getEngineeringControl(Long sdsId) {

        EngineeringControl control = engineeringControlRepository
                .findBySection8_SdsId(sdsId)
                .orElseThrow(() ->
                        new RuntimeException("Engineering Control not found for SDS ID: " + sdsId)
                );

        return EngineeringControlResponseDTO.builder()
                .id(control.getId())
                .sdsId(control.getSection8().getSdsId())
                .description(control.getDescription())
                .build();
    }

    /* ================= UPSERT Engineering Controls ================= */
    @Override
    public EngineeringControlResponseDTO upsertEngineeringControl(
            Long sdsId,
            EngineeringControlRequestDTO requestDTO) {

        if (sdsId == null) {
            throw new RuntimeException("SDS ID cannot be null");
        }

        // 1. Get or create Section8
        Section8 section8 = section8Repository.findBySdsId(sdsId)
                .orElseGet(() -> {
                    Section8 newSection = new Section8();
                    newSection.setSdsId(sdsId);
                    return section8Repository.save(newSection);
                });

        // 2. Fetch existing using repository (BETTER)
        EngineeringControl control = engineeringControlRepository
                .findBySection8_SdsId(sdsId)
                .orElse(null);

        if (control == null) {
            control = new EngineeringControl();
            control.setSection8(section8);
        }

        // 3. Update
        control.setDescription(requestDTO.getDescription());

        // 4. Save
        EngineeringControl saved = engineeringControlRepository.save(control);

        // 5. Response
        return EngineeringControlResponseDTO.builder()
                .id(saved.getId())
                .sdsId(sdsId)
                .description(saved.getDescription())
                .build();
    }
}