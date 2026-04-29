package com.clideOffice.clideApp.common.ocr_project.sds.Section9.ServiceImpl;

import com.clideOffice.clideApp.common.ocr_project.sds.Section9.Service.Section9Service;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section9.PhysicalChemicalProperties;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.Section9Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section9ResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section9RequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Section9ServiceImpl implements Section9Service {

    private final Section9Repository repository;

    // POST
    @Override
    public Section9ResponseDTO addOrUpdate(Section9RequestDTO requestDTO) {

        PhysicalChemicalProperties entity = repository.findBySdsId(requestDTO.getSdsId())
                .orElse(PhysicalChemicalProperties.builder()
                        .sdsId(requestDTO.getSdsId())
                        .build());

        // 🔹 Map fields
        entity.setPhysicalState(requestDTO.getPhysicalState());
        entity.setColour(requestDTO.getColour());
        entity.setOdour(requestDTO.getOdour());
        entity.setMeltingPoint(requestDTO.getMeltingPoint());
        entity.setBoilingPoint(requestDTO.getBoilingPoint());
        entity.setFlammability(requestDTO.getFlammability());
        entity.setExplosionLimit(requestDTO.getExplosionLimit());
        entity.setFlashPoint(requestDTO.getFlashPoint());
        entity.setAutoIgnitionTemperature(requestDTO.getAutoIgnitionTemperature());
        entity.setDecompositionTemperature(requestDTO.getDecompositionTemperature());
        entity.setPh(requestDTO.getPh());
        entity.setKinematicViscosity(requestDTO.getKinematicViscosity());
        entity.setSolubility(requestDTO.getSolubility());
        entity.setPartitionCoefficient(requestDTO.getPartitionCoefficient());

        entity.setVapourPressure(requestDTO.getVapourPressure());
        entity.setDensity(requestDTO.getDensity());
        entity.setRelativeVapourDensity(requestDTO.getRelativeVapourDensity());
        entity.setParticleCharacteristics(requestDTO.getParticleCharacteristics());
        entity.setParticleSize(requestDTO.getParticleSize());
        entity.setExplosiveProperties(requestDTO.getExplosiveProperties());
        entity.setOxidisingProperties(requestDTO.getOxidisingProperties());
        entity.setEvaporationRate(requestDTO.getEvaporationRate());
        entity.setViscosity(requestDTO.getViscosity());
        entity.setBulkDensity(requestDTO.getBulkDensity());
        entity.setMoisture(requestDTO.getMoisture());
        entity.setVocContent(requestDTO.getVocContent());

        entity.setOtherInformation(requestDTO.getOtherInformation());

        // 🔹 Save
        PhysicalChemicalProperties saved = repository.save(entity);

        return mapToResponse(saved);
    }

    /* ================= GET SECTION 9 ================= */
    @Override
    public Section9ResponseDTO getBySdsId(Long sdsId) {

        log.info("Fetching Section 9 data for SDS ID: {}", sdsId);

        PhysicalChemicalProperties entity = repository.findBySdsId(sdsId)
                .orElseThrow(() -> {
                    log.error("Section 9 data not found for SDS ID: {}", sdsId);
                    return new RuntimeException("Section 9 data not found for SDS ID: " + sdsId);
                });

        return mapToResponse(entity);
    }

    /* ================= ENTITY → DTO MAPPING ================= */
    private Section9ResponseDTO mapToResponse(PhysicalChemicalProperties e) {

        return Section9ResponseDTO.builder()
                .sdsId(e.getSdsId())

                // 🔹 9.1 Properties
                .physicalState(e.getPhysicalState())
                .colour(e.getColour())
                .odour(e.getOdour())
                .meltingPoint(e.getMeltingPoint())
                .boilingPoint(e.getBoilingPoint())
                .flammability(e.getFlammability())
                .explosionLimit(e.getExplosionLimit())
                .flashPoint(e.getFlashPoint())
                .autoIgnitionTemperature(e.getAutoIgnitionTemperature())
                .decompositionTemperature(e.getDecompositionTemperature())
                .ph(e.getPh())
                .kinematicViscosity(e.getKinematicViscosity())
                .solubility(e.getSolubility())
                .partitionCoefficient(e.getPartitionCoefficient())

                .vapourPressure(e.getVapourPressure())
                .density(e.getDensity())
                .relativeVapourDensity(e.getRelativeVapourDensity())
                .particleCharacteristics(e.getParticleCharacteristics())
                .particleSize(e.getParticleSize())
                .explosiveProperties(e.getExplosiveProperties())
                .oxidisingProperties(e.getOxidisingProperties())
                .evaporationRate(e.getEvaporationRate())
                .viscosity(e.getViscosity())
                .bulkDensity(e.getBulkDensity())
                .moisture(e.getMoisture())
                .vocContent(e.getVocContent())

                // 🔹 9.2
                .otherInformation(e.getOtherInformation())

                .build();
    }

    // Update
    @Override
    public Section9ResponseDTO update(Long id, Section9RequestDTO requestDTO) {

        PhysicalChemicalProperties entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section 9 not found with id: " + id));

        // 🔹 Update all fields
        entity.setPhysicalState(requestDTO.getPhysicalState());
        entity.setColour(requestDTO.getColour());
        entity.setOdour(requestDTO.getOdour());
        entity.setMeltingPoint(requestDTO.getMeltingPoint());
        entity.setBoilingPoint(requestDTO.getBoilingPoint());
        entity.setFlammability(requestDTO.getFlammability());
        entity.setExplosionLimit(requestDTO.getExplosionLimit());
        entity.setFlashPoint(requestDTO.getFlashPoint());
        entity.setAutoIgnitionTemperature(requestDTO.getAutoIgnitionTemperature());
        entity.setDecompositionTemperature(requestDTO.getDecompositionTemperature());
        entity.setPh(requestDTO.getPh());
        entity.setKinematicViscosity(requestDTO.getKinematicViscosity());
        entity.setSolubility(requestDTO.getSolubility());
        entity.setPartitionCoefficient(requestDTO.getPartitionCoefficient());

        entity.setVapourPressure(requestDTO.getVapourPressure());
        entity.setDensity(requestDTO.getDensity());
        entity.setRelativeVapourDensity(requestDTO.getRelativeVapourDensity());
        entity.setParticleCharacteristics(requestDTO.getParticleCharacteristics());
        entity.setParticleSize(requestDTO.getParticleSize());
        entity.setExplosiveProperties(requestDTO.getExplosiveProperties());
        entity.setOxidisingProperties(requestDTO.getOxidisingProperties());
        entity.setEvaporationRate(requestDTO.getEvaporationRate());
        entity.setViscosity(requestDTO.getViscosity());
        entity.setBulkDensity(requestDTO.getBulkDensity());
        entity.setMoisture(requestDTO.getMoisture());
        entity.setVocContent(requestDTO.getVocContent());

        entity.setOtherInformation(requestDTO.getOtherInformation());

        PhysicalChemicalProperties updated = repository.save(entity);

        return mapToResponse(updated);
    }
}