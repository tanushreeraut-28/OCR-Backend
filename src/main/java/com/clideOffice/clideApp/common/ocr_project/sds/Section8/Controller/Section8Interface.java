package com.clideOffice.clideApp.common.ocr_project.sds.Section8.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.EngineeringControlRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.ExposureLimitRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.EngineeringControlResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.ExposureLimitResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/section8")
@Tag(name = "SDS Section 8 API", description = "Exposure Controls / Personal Protection")
public interface Section8Interface {

    /* ================= POST ================= */
    @Operation(summary = "Add Exposure Limit", description = "Add exposure limit for given SDS ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Data saved successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/{sdsId}/exposure-limits", produces = "application/json")
    ResponseEntity<ExposureLimitResponseDTO> addExposureLimit(@PathVariable Long sdsId, @RequestBody ExposureLimitRequestDTO requestDTO);

    /* ================= GET Exposure Limits ================= */
    @Operation(summary = "Get Exposure Limits", description = "Fetch all exposure limits for given SDS ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Data fetched successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping(value = "/{sdsId}/exposure-limits", produces = "application/json")
    ResponseEntity<List<ExposureLimitResponseDTO>> getExposureLimits(@PathVariable Long sdsId);

    /* ================= UPDATE Exposure Limit ================= */
    @Operation(summary = "Update Exposure Limit", description = "Update exposure limit by ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Data fetched successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PutMapping(value = "/exposure-limits/{id}", produces = "application/json")
    ResponseEntity<ExposureLimitResponseDTO> updateExposureLimit(@PathVariable Long id, @RequestBody ExposureLimitRequestDTO requestDTO);

    /* ================= DELETE Exposure Limit ================= */
    @Operation(summary = "Delete Exposure Limit", description = "Delete exposure limit by ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Exposure Limit deleted successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @DeleteMapping(value = "/exposure-limits/{id}", produces = "application/json")
    ResponseEntity<String> deleteExposureLimit(@PathVariable Long id);

    /* ================= GET Engineering Controls ================= */
    @Operation(summary = "Get Engineering Controls", description = "Fetch engineering controls by SDS ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Exposure Limit deleted successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping(value = "/{sdsId}/engineering-controls", produces = "application/json")
    ResponseEntity<EngineeringControlResponseDTO> getEngineeringControl(@PathVariable Long sdsId);

    /* ================= UPSERT Engineering Controls ================= */
    @Operation(summary = "Add/Update Engineering Controls", description = "Create or update engineering controls")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Exposure Limit deleted successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PutMapping(value = "/{sdsId}/engineering-controls", produces = "application/json")
    ResponseEntity<EngineeringControlResponseDTO> upsertEngineeringControl(@PathVariable Long sdsId, @RequestBody EngineeringControlRequestDTO requestDTO
    );
}