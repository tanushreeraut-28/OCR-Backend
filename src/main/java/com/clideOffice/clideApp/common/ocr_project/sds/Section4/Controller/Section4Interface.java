package com.clideOffice.clideApp.common.ocr_project.sds.Section4.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SpecialTreatmentRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.FirstAidMeasuresResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SpecialTreatmentResponseDTO;
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
@RequestMapping("/api/first-aid")
@Tag(name = "SDS Section 4 API", description = "Operations related to SDS Section 4")
public interface Section4Interface {

    default Optional<NativeWebRequest> getRequest() {return Optional.empty();}

    /* ================= GET First Aid Measures ================= */
    @Operation(summary = "Get First Aid Measures by SDS ID", description = "Fetch first aid information for given SDS")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Data fetched successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping(value = "/{sdsId}", produces = "application/json")
    ResponseEntity<FirstAidMeasuresResponseDTO> getFirstAidMeasures(@PathVariable Long sdsId);

    /* ================= ADD Special Treatment ================= */
    @Operation(summary = "Add Special Treatment", description = "Add special treatment details for given SDS")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Special Treatment added successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/special-treatment", produces = "application/json")
    ResponseEntity<SpecialTreatmentResponseDTO> addSpecialTreatment(@RequestBody SpecialTreatmentRequestDTO requestDTO);
}