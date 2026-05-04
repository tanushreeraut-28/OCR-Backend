package com.clideOffice.clideApp.common.ocr_project.sds.Section3.controller;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SpecialLimitRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UpdateIngredientRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.IngredientResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SpecialLimitResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Upload3and4ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

@Validated
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/ingredients")
@Tag(name = "SDS Section 3 API", description = "Operations related to SDS Section 3")
public interface SdsInterface {

    default Optional<NativeWebRequest> getRequest() { return Optional.empty(); }

    /* ================= SDS Upload API ================= */
    @Operation(summary = "Upload SDS Section 3 & 4", description = "Upload SDS document for Section 3 and 4")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Upload Success"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(value = "/upload-3-4",
            consumes = "multipart/form-data",
            produces = "application/json")
    public ResponseEntity<Upload3and4ResponseDto> uploadSds(
            @ModelAttribute UploadRequestDto request);

    /* ================= GET Ingredients by SDS ================= */
    @Operation(summary = "Get Ingredients by SDS ID", description = "Fetch all ingredients for a given SDS ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingredients fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping(value = "/sds/{sdsId}", produces = "application/json")
    ResponseEntity<List<IngredientResponseDTO>> getIngredientsBySdsId(@PathVariable Long sdsId);

    /* ================= UPDATE Ingredient ================= */
    @Operation(summary = "Update Ingredient", description = "Update ingredient details by ingredient ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingredient updated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping(value = "/{ingredientId}", consumes = "application/json", produces = "application/json")
    ResponseEntity<IngredientResponseDTO> updateIngredient(
            @PathVariable Long ingredientId,
            @RequestBody UpdateIngredientRequestDTO request
    );

    /* ================= DELETE Ingredient ================= */
    @Operation(summary = "Delete Ingredient", description = "Delete ingredient by ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Ingredient deleted successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @DeleteMapping(value = "/{ingredientId}", produces = "application/json")
    ResponseEntity<String> deleteIngredient(@PathVariable Long ingredientId);

    /* ================= CREATE Special Limit ================= */
    @Operation(summary = "Create Special Limit", description = "Add new special limit for SDS")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Special limit created successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/special-limits", consumes = "application/json", produces = "application/json")
    ResponseEntity<SpecialLimitResponseDTO> createSpecialLimit(@RequestBody SpecialLimitRequestDTO request);

    /* ================= GET Special Limits ================= */
    @Operation(summary = "Get Special Limits by SDS ID", description = "Fetch all special limits for a given SDS ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Special limits fetched successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping(value = "/special-limits/{sdsId}", produces = "application/json")
    ResponseEntity<List<SpecialLimitResponseDTO>> getSpecialLimitsBySdsId(@PathVariable Long sdsId);

}