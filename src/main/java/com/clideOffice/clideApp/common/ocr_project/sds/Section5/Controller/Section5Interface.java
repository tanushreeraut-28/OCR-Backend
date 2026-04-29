package com.clideOffice.clideApp.common.ocr_project.sds.Section5.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section5FirefightingRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section5FirefightingResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@Validated
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/firefighting")
@Tag(name = "SDS Section 5 API", description = "Operations related to SDS Section ")
public interface Section5Interface {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /* ================= SAVE / UPDATE FIREFIGHTING ================= */
    @Operation(
            summary = "Save or Update Firefighting Section",
            description = "Create or update Section 5 (Firefighting Measures) for given SDS ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Saved/Updated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    ResponseEntity<Section5FirefightingResponseDTO> saveOrUpdateFirefighting(
            @RequestBody Section5FirefightingRequestDTO requestDTO
    );

    /* ================= GET FIREFIGHTING BY SDS ID ================= */
    @Operation(
            summary = "Get Firefighting Data by SDS ID",
            description = "Fetch Section 5 (Firefighting Measures) data for given SDS ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Data fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping(value = "/sds/{sdsId}", produces = "application/json")
    ResponseEntity<Section5FirefightingResponseDTO> getFirefightingBySdsId(
            @PathVariable Long sdsId
    );
}