package com.clideOffice.clideApp.common.ocr_project.sds.section7.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SafeHandlingRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SafeStorageRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SpecificEndUseRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SafeHandlingResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SafeStorageResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SpecificEndUseResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/section7")
@Tag(name = "SDS Section 7 API", description = "Precautions for Safe Handling")
public interface Section7Interface {

    /* ================= GET ================= */
    @Operation(summary = "Get Safe Handling by SDS ID", description = "Fetch precautions for safe handling for given SDS ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Data fetched successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping(value = "/handling/{sdsId}", produces = "application/json")
    ResponseEntity<SafeHandlingResponseDTO> getSafeHandling(@PathVariable Long sdsId);

    /* ================= ADD ================= */
    @Operation(summary = "Add Safe Handling", description = "Add precautions for safe handling")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Data added successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/handling", produces = "application/json")
    ResponseEntity<SafeHandlingResponseDTO> addSafeHandling(@RequestBody SafeHandlingRequestDTO requestDTO);

    /* ================= UPDATE ================= */
    @Operation(summary = "Update Safe Handling", description = "Update precautions for safe handling by ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Data updated successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PutMapping(value = "/handling/{id}", produces = "application/json")
    ResponseEntity<SafeHandlingResponseDTO> updateSafeHandling(@PathVariable Long id, @RequestBody SafeHandlingRequestDTO requestDTO);

    /* ================= GET Safe Storage ================= */
    @Operation(summary = "Get Storage Conditions by SDS ID", description = "Fetch storage conditions")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Data fetched successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping(value = "/storage/{sdsId}", produces = "application/json")
    ResponseEntity<SafeStorageResponseDTO> getStorageConditions(@PathVariable Long sdsId);

    /* ================= ADD / UPDATE Safe Storage  ================= */
    @Operation(summary = "Add or Update Storage Conditions", description = "UPSERT storage conditions")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Data saved successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/storage", produces = "application/json")
    ResponseEntity<SafeStorageResponseDTO> addOrUpdate(@RequestBody SafeStorageRequestDTO requestDTO);

    /* ================= GET Specific End Use ================= */
    @Operation(summary = "Get Specific End Use", description = "Fetch specific end use by SDS ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Error")
    })
    @GetMapping(value = "/end-use/{sdsId}", produces = "application/json")
    ResponseEntity<SpecificEndUseResponseDTO> getSpecificEndUse(@PathVariable Long sdsId);

    /* ================= Edit Specific End Use ================= */
    @Operation(summary = "Add or Update Specific End Use", description = "UPSERT specific end use")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Saved"),
            @ApiResponse(responseCode = "500", description = "Error")
    })
    @PutMapping(value = "/end-use/{sdsId}", produces = "application/json")
    ResponseEntity<SpecificEndUseResponseDTO> addOrUpdateSpecificEndUse(
            @PathVariable Long sdsId,
            @RequestBody SpecificEndUseRequestDTO requestDTO);
}

