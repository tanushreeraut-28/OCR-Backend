package com.clideOffice.clideApp.common.ocr_project.sds.section7.ServiceImpl;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section7.SafeHandling;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section7.SafeStorage;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section7.SpecificEndUse;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.SafeHandlingRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.SpecificEndUseRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.StorageConditionsRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SafeHandlingRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SafeStorageRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SpecificEndUseRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SafeStorageResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SafeHandlingResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SpecificEndUseResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.section7.Service.Section7Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Section7ServiceImpl implements Section7Service {

    private final SafeHandlingRepository safeHandlingRepository;
    private final StorageConditionsRepository storageConditionsRepository;
    private final SpecificEndUseRepository specificEndUseRepository;

    //  GET Safe Handling
    @Override
    public SafeHandlingResponseDTO getSafeHandling(Long sdsId) {
        SafeHandling entity = safeHandlingRepository.findBySdsId(sdsId)
                .orElseThrow(() -> new RuntimeException("Safe Handling not found for SDS ID: " + sdsId));

        return mapToResponse(entity);
    }

    // ADD Safe Handling
    @Override
    public SafeHandlingResponseDTO addSafeHandling(SafeHandlingRequestDTO requestDTO) {

        // Check if already exists → UPDATE instead of INSERT
        SafeHandling entity = safeHandlingRepository.findBySdsId(requestDTO.getSdsId())
                .orElse(
                        SafeHandling.builder()
                                .sdsId(requestDTO.getSdsId())
                                .build()
                );

        entity.setPrecautions(requestDTO.getPrecautions());
        entity.setHygieneMeasures(requestDTO.getHygieneMeasures());
        entity.setProtectiveMeasures(requestDTO.getProtectiveMeasures());
        entity.setFirePrevention(requestDTO.getFirePrevention());

        SafeHandling saved = safeHandlingRepository.save(entity);

        return mapToResponse(saved);
    }

    // UPDATE Safe Handling
    @Override
    public SafeHandlingResponseDTO updateSafeHandling(Long id, SafeHandlingRequestDTO requestDTO) {

        // Ignore id → use sdsId for safety
        SafeHandling entity = safeHandlingRepository.findBySdsId(requestDTO.getSdsId())
                .orElseThrow(() -> new RuntimeException("Safe Handling not found for SDS ID: " + requestDTO.getSdsId()));

        entity.setPrecautions(requestDTO.getPrecautions());
        entity.setHygieneMeasures(requestDTO.getHygieneMeasures());
        entity.setProtectiveMeasures(requestDTO.getProtectiveMeasures());
        entity.setFirePrevention(requestDTO.getFirePrevention());

        SafeHandling updated = safeHandlingRepository.save(entity);

        return mapToResponse(updated);
    }

    /* ================= MAPPER for Safe Handling ================= */
    private SafeHandlingResponseDTO mapToResponse(SafeHandling entity) {
        return SafeHandlingResponseDTO.builder()
                .id(entity.getId())
                .sdsId(entity.getSdsId())
                .precautions(entity.getPrecautions())
                .hygieneMeasures(entity.getHygieneMeasures())
                .protectiveMeasures(entity.getProtectiveMeasures())
                .firePrevention(entity.getFirePrevention())
                .build();
    }

    // GET Safe Storage
    @Override
    public SafeStorageResponseDTO getStorageConditions(Long sdsId) {

        return storageConditionsRepository.findBySdsId(sdsId)
                .map(this::mapToStorageResponse)
                .orElseGet(() -> SafeStorageResponseDTO.builder()
                        .sdsId(sdsId)
                        .storageConditions(null)
                        .incompatibleMaterials(null)
                        .storageTemperature(null)
                        .ventilation(null)
                        .additionalInformation(null)
                        .build());
    }

    // ADD / UPDATE Safe Storage (UPSERT)
    @Override
    public SafeStorageResponseDTO addOrUpdate(SafeStorageRequestDTO requestDTO) {

        SafeStorage entity = storageConditionsRepository.findBySdsId(requestDTO.getSdsId())
                .orElse(SafeStorage.builder()
                        .sdsId(requestDTO.getSdsId())
                        .build());

        entity.setStorageConditions(requestDTO.getStorageConditions());
        entity.setIncompatibleMaterials(requestDTO.getIncompatibleMaterials());
        entity.setStorageTemperature(requestDTO.getStorageTemperature());
        entity.setVentilation(requestDTO.getVentilation());
        entity.setAdditionalInformation(requestDTO.getAdditionalInformation());

        SafeStorage saved = storageConditionsRepository.save(entity);

        return mapToStorageResponse(saved);
    }

    /* ================= MAPPER Safe Storage ================= */
    private SafeStorageResponseDTO mapToStorageResponse(SafeStorage entity) {
        return SafeStorageResponseDTO.builder()
                .id(entity.getId())
                .sdsId(entity.getSdsId())
                .storageConditions(entity.getStorageConditions())
                .incompatibleMaterials(entity.getIncompatibleMaterials())
                .storageTemperature(entity.getStorageTemperature())
                .ventilation(entity.getVentilation())
                .additionalInformation(entity.getAdditionalInformation())
                .build();
    }

    // GET Specific End Use
    @Override
    public SpecificEndUseResponseDTO getSpecificEndUse(Long sdsId) {

        SpecificEndUse entity = specificEndUseRepository.findBySdsId(sdsId)
                .orElseThrow(() -> new RuntimeException("Specific End Use not found for SDS ID: " + sdsId));

        return mapToEndUseResponse(entity);
    }

    // EDIT Specific End Use
    @Override
    public SpecificEndUseResponseDTO addOrUpdateSpecificEndUse(Long sdsId, SpecificEndUseRequestDTO requestDTO) {

        SpecificEndUse entity = specificEndUseRepository.findBySdsId(sdsId)
                .orElse(SpecificEndUse.builder()
                        .sdsId(sdsId)
                        .build());

        entity.setSpecificEndUse(requestDTO.getSpecificEndUse());

        SpecificEndUse saved = specificEndUseRepository.save(entity);

        return mapToEndUseResponse(saved);
    }

    /* ================= MAPPER Specific End Use ================= */
    private SpecificEndUseResponseDTO mapToEndUseResponse(SpecificEndUse entity) {
        return SpecificEndUseResponseDTO.builder()
                .id(entity.getId())
                .sdsId(entity.getSdsId())
                .specificEndUse(entity.getSpecificEndUse())
                .build();
    }
}