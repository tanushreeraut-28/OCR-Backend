package com.clideOffice.clideApp.common.ocr_project.sds.Section3.service;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SpecialLimitRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UpdateIngredientRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.IngredientResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SpecialLimitResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Upload3and4ResponseDto;

import java.util.List;

public interface Section3Service {

    // Upload Section 3 and 4
    Upload3and4ResponseDto uploadSection3and4(UploadRequestDto request);

    // GET Ingredients by SDS
    List<IngredientResponseDTO> getIngredientsBySdsId(Long sdsId);

    // Update Ingredient
    IngredientResponseDTO  updateIngredient(Long ingredientId, UpdateIngredientRequestDTO request);

    // Delete Ingredient
    void deleteIngredient(Long ingredientId);

    // CREATE Special Limit
    SpecialLimitResponseDTO createSpecialLimit(SpecialLimitRequestDTO request);

    // GET Special Limits
    List<SpecialLimitResponseDTO> getSpecialLimitsBySdsId(Long sdsId);
}
