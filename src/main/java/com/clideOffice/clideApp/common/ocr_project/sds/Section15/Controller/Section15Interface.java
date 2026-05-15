package com.clideOffice.clideApp.common.ocr_project.sds.Section15.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section15RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section15ResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadSdsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/section15")
@Tag(name = "SDS Section 15 API", description = "Operations related to SDS Section 15")
public interface Section15Interface {

    /* ================= SDS Upload API 15 & 16 ================= */
    @Operation(summary = "Upload SDS File", description = "Upload SDS document")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Upload Success"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/upload-15-16", consumes = "multipart/form-data", produces = "application/json")
    ResponseEntity<UploadSdsResponseDto> uploadSection15And16(@ModelAttribute UploadRequestDto request);

    /* ================= POST ================= */
    @Operation(summary = "Add or Update Section15")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Data saved successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/item", produces = "application/json")
    ResponseEntity<Section15ResponseDTO> addItem(@RequestBody Section15RequestDTO requestDTO);

    /* ================= GET ================= */
    @Operation(summary = "Get Section15 by SDS ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Data fetched successfully"), @ApiResponse(responseCode = "404", description = "Data not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping(value = "/{sdsId}", produces = "application/json")
    ResponseEntity<Section15ResponseDTO> getBySdsId(@PathVariable Long sdsId);

    /* ================= DELETE ================= */
    @Operation(summary = "Delete Section15")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Deleted successfully"), @ApiResponse(responseCode = "404", description = "Data not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @DeleteMapping(value = "/item/{id}", produces = "application/json")
    ResponseEntity<String> deleteItem(@PathVariable Long id);
}