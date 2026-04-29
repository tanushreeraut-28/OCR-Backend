package com.clideOffice.clideApp.common.ocr_project.sds.Section3.serviceImpl;

import com.clideOffice.clideApp.common.ocr_project.sds.Section3.service.Section3Service;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section3.Ingredient;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section3.SpecialLimit;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.IngredientRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.repository.SpecialLimitRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SpecialLimitRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UpdateIngredientRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.IngredientResponseDTO;

import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SpecialLimitResponseDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Section3ServiceImpl implements Section3Service {

    private final IngredientRepository ingredientRepository;
    private final SpecialLimitRepository specialLimitRepository;

    // GET Ingredients by SDS
    @Override
    public List<IngredientResponseDTO> getIngredientsBySdsId(Long sdsId) {
        List<Ingredient> ingredients = ingredientRepository.findBySdsIdAndIsActiveTrue(sdsId);

        return ingredients.stream()
                .map(ingredient -> {

                    String concentration;

                    Double min = ingredient.getConcentrationMin();
                    Double max = ingredient.getConcentrationMax();

                    if (min == null && max == null) {
                        concentration = null;
                    } else if (min != null && max != null) {
                        concentration = min.intValue() + " - " + max.intValue() + "%";
                    } else if (min != null) {
                        concentration = "≥ " + min.intValue() + "%";
                    } else {
                        concentration = "< " + max.intValue() + "%";
                    }

                    return IngredientResponseDTO.builder()
                            .id(ingredient.getId())
                            .chemicalName(ingredient.getChemicalName())
                            .casNumber(ingredient.getCasNumber())
                            .ecNumber(ingredient.getEcNumber())
                            .concentration(concentration)
                            .classification(ingredient.getClassification())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // UPDATE Ingredient
    @Override
    public IngredientResponseDTO updateIngredient(Long ingredientId, UpdateIngredientRequestDTO request) {

        // 1. Fetch ingredient from DB
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + ingredientId));

        // 2. Update fields
        ingredient.setChemicalName(request.getChemicalName());
        ingredient.setCasNumber(request.getCasNumber());
        ingredient.setEcNumber(request.getEcNumber());
        ingredient.setConcentrationMin(request.getConcentrationMin());
        ingredient.setConcentrationMax(request.getConcentrationMax());
        ingredient.setClassification(request.getClassification());

        // 3. Update timestamp
        ingredient.setUpdatedAt(java.time.LocalDateTime.now());

        // 4. Save updated entity
        Ingredient saved = ingredientRepository.save(ingredient);

        // 5. Convert to ResponseDTO
        String concentration;

        if (saved.getConcentrationMin() == null && saved.getConcentrationMax() == null) {
            concentration = null;
        } else if (saved.getConcentrationMin() != null && saved.getConcentrationMax() != null) {
            concentration = saved.getConcentrationMin().intValue() + " - " +
                    saved.getConcentrationMax().intValue() + "%";
        } else if (saved.getConcentrationMin() != null) {
            concentration = "≥ " + saved.getConcentrationMin().intValue() + "%";
        } else {
            concentration = "< " + saved.getConcentrationMax().intValue() + "%";
        }

        return IngredientResponseDTO.builder()
                .id(saved.getId())
                .chemicalName(saved.getChemicalName())
                .casNumber(saved.getCasNumber())
                .ecNumber(saved.getEcNumber())
                .concentration(concentration)
                .classification(saved.getClassification())
                .build();
    }

    // Delete Ingredients
    @Override
    public void deleteIngredient(Long ingredientId) {

        // 1. Fetch ingredient
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + ingredientId));

        // 2. Soft delete (mark inactive)
        ingredient.setIsActive(false);

        // 3. Update timestamp
        ingredient.setUpdatedAt(java.time.LocalDateTime.now());

        // 4. Save changes
        ingredientRepository.save(ingredient);
    }

    // Create Special Limit
    @Override
    public SpecialLimitResponseDTO createSpecialLimit(SpecialLimitRequestDTO request) {

        // 1. Convert DTO → Entity
        SpecialLimit specialLimit = SpecialLimit.builder()
                .sdsId(request.getSdsId())
                .substanceName(request.getSubstanceName())
                .limitValue(request.getLimitValue())
                .hazardClass(request.getHazardClass())
                .remarks(request.getRemarks())
                .isActive(true)
                .build();

        // 2. Save to DB
        SpecialLimit saved = specialLimitRepository.save(specialLimit);

        // 3. Convert Entity → ResponseDTO
        return SpecialLimitResponseDTO.builder()
                .id(saved.getId())
                .sdsId(saved.getSdsId())
                .substanceName(saved.getSubstanceName())
                .limitValue(saved.getLimitValue())
                .hazardClass(saved.getHazardClass())
                .remarks(saved.getRemarks())
                .build();
    }

    // GET Special Limits
    @Override
    public List<SpecialLimitResponseDTO> getSpecialLimitsBySdsId(Long sdsId) {

        // 1. Fetch from DB (only active records)
        List<SpecialLimit> specialLimits =
                specialLimitRepository.findBySdsIdAndIsActiveTrue(sdsId);

        // 2. Convert Entity → DTO
        return specialLimits.stream()
                .map(limit -> SpecialLimitResponseDTO.builder()
                        .id(limit.getId())
                        .sdsId(limit.getSdsId())
                        .substanceName(limit.getSubstanceName())
                        .limitValue(limit.getLimitValue())
                        .hazardClass(limit.getHazardClass())
                        .remarks(limit.getRemarks())
                        .build())
                .toList();
    }
}