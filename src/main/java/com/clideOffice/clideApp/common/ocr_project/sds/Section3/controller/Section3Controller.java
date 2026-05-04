package com.clideOffice.clideApp.common.ocr_project.sds.Section3.controller;

import com.clideOffice.clideApp.common.ocr_project.sds.Section3.service.Section3Service;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SpecialLimitRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UpdateIngredientRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.IngredientResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SpecialLimitResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Upload3and4ResponseDto;
import com.clideOffice.clideApp.common.ocr_project.util.DatabaseContextHolder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.clideOffice.clideApp.common.ocr_project.sds.Section1and2.controller.SdsController.logger;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Section3Controller implements SdsInterface {

    private final Section3Service section3Service;

    /* ================= SDS Upload API ================= */
    @Override
    public ResponseEntity<Upload3and4ResponseDto> uploadSds(UploadRequestDto request) {
        try {
            Upload3and4ResponseDto response = section3Service.uploadSection3and4(request);

            // ✅ HANDLE DIFFERENT STATUSES
            if ("DUPLICATE".equalsIgnoreCase(response.getStatus())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            if ("OCR_FAILED".equalsIgnoreCase(response.getStatus())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error occurred while uploading SDS Section 3 & 4", e);

            Upload3and4ResponseDto error = new Upload3and4ResponseDto();
            error.setStatus("FAILED");
            error.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error);

        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= GET Ingredients by SDS ================= */
    @Override
    public ResponseEntity<List<IngredientResponseDTO>> getIngredientsBySdsId(Long sdsId) {
        try {
            return ResponseEntity.ok(section3Service.getIngredientsBySdsId(sdsId));
        } catch (Exception e) {
            log.error("Error fetching ingredients for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= UPDATE Ingredient ================= */
    @Override
    public ResponseEntity<IngredientResponseDTO> updateIngredient(
            Long ingredientId,
            UpdateIngredientRequestDTO request) {

        try {
            return ResponseEntity.ok(
                    section3Service.updateIngredient(ingredientId, request)
            );
        } catch (Exception e) {
            log.error("Error updating ingredient ID: {}", ingredientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= DELETE Ingredient ================= */
    @Override
    public ResponseEntity<String> deleteIngredient(Long ingredientId) {
        try {
            section3Service.deleteIngredient(ingredientId);
            return ResponseEntity.ok("Ingredient deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting ingredient ID: {}", ingredientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting ingredient");
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    @Override
    public ResponseEntity<SpecialLimitResponseDTO> createSpecialLimit(
            @RequestBody SpecialLimitRequestDTO request) {

        try {
            SpecialLimitResponseDTO response =
                    section3Service.createSpecialLimit(request);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error occurred while creating special limit", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            DatabaseContextHolder.clear();
        }
    }

    /* ================= GET Special Limits ================= */
    @Override
    public ResponseEntity<List<SpecialLimitResponseDTO>> getSpecialLimitsBySdsId(@PathVariable Long sdsId) {
        try {
            List<SpecialLimitResponseDTO> response = section3Service.getSpecialLimitsBySdsId(sdsId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error occurred while fetching special limits for SDS ID: {}", sdsId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            DatabaseContextHolder.clear();
        }
    }
}