package com.clideOffice.clideApp.common.ocr_project.sds.Section5.ServiceImpl;

import com.clideOffice.clideApp.common.ocr_project.sds.Section5.Service.Section5Service;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section5.Section5Firefighting;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.Section5Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section5FirefightingRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section5FirefightingResponseDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Section5ServiceImpl implements Section5Service {

    private final Section5Repository repository;

    // SAVE / UPDATE FIREFIGHTING
    @Override
    public Section5FirefightingResponseDTO saveOrUpdateFirefighting(
            Section5FirefightingRequestDTO requestDTO) {

        LocalDateTime now = LocalDateTime.now();

        Optional<Section5Firefighting> optional =
                repository.findBySdsId(requestDTO.getSdsId());

        Section5Firefighting entity;

        if (optional.isPresent()) {
            // UPDATE
            entity = optional.get();

            entity.setSuitableMedia(requestDTO.getSuitableMedia());
            entity.setUnsuitableMedia(requestDTO.getUnsuitableMedia());
            entity.setSpecialHazards(requestDTO.getSpecialHazards());
            entity.setAdvice(requestDTO.getAdvice());
            entity.setProtectiveEquipment(requestDTO.getProtectiveEquipment());
            entity.setUpdatedAt(now);

        } else {
            // CREATE
            entity = Section5Firefighting.builder()
                    .sdsId(requestDTO.getSdsId())
                    .suitableMedia(requestDTO.getSuitableMedia())
                    .unsuitableMedia(requestDTO.getUnsuitableMedia())
                    .specialHazards(requestDTO.getSpecialHazards())
                    .advice(requestDTO.getAdvice())
                    .protectiveEquipment(requestDTO.getProtectiveEquipment())
                    .isActive(true)
                    .createdAt(now)
                    .updatedAt(now) // ✅ FIXED
                    .build();
        }

        Section5Firefighting saved = repository.save(entity);

        // RESPONSE
        return Section5FirefightingResponseDTO.builder()
                .id(saved.getId())
                .sdsId(saved.getSdsId())
                .suitableMedia(saved.getSuitableMedia())
                .unsuitableMedia(saved.getUnsuitableMedia())
                .specialHazards(saved.getSpecialHazards())
                .advice(saved.getAdvice())
                .protectiveEquipment(saved.getProtectiveEquipment())
                .isActive(saved.getIsActive())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .message(optional.isPresent() ? "Updated successfully" : "Saved successfully")
                .build();
    }

    // GET FIREFIGHTING BY SDS ID
    @Override
    public Section5FirefightingResponseDTO getFirefightingBySdsId(Long sdsId) {

        Section5Firefighting entity = repository.findBySdsId(sdsId)
                .orElseThrow(() -> new RuntimeException("Firefighting data not found"));

        return Section5FirefightingResponseDTO.builder()
                .id(entity.getId())
                .sdsId(entity.getSdsId())
                .suitableMedia(entity.getSuitableMedia())
                .unsuitableMedia(entity.getUnsuitableMedia())
                .specialHazards(entity.getSpecialHazards())
                .advice(entity.getAdvice())
                .protectiveEquipment(entity.getProtectiveEquipment())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .message("Data fetched successfully")
                .build();
    }
}