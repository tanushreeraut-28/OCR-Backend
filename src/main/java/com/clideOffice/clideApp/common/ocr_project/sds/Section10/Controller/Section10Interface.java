package com.clideOffice.clideApp.common.ocr_project.sds.Section10.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section10RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section10ResponseDTO;
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
@RequestMapping("/api/section10")
@Tag(name = "SDS Section 10 API", description = "Operations related to SDS Section 10")
public interface Section10Interface {

    /* ================= SDS Upload API 7 & 8 ================= */
    @Operation(summary = "Upload SDS File", description = "Upload SDS document")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Upload Success"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/upload-7-8", consumes = "multipart/form-data", produces = "application/json")
    ResponseEntity<UploadSdsResponseDto> uploadSection7And8(@ModelAttribute UploadRequestDto request);

    /* ================= SDS Upload API 12 & 13 & 14 ================= */
    @Operation(summary = "Upload SDS File", description = "Upload SDS document")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Upload Success"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/upload-12-13-14", consumes = "multipart/form-data", produces = "application/json")
    ResponseEntity<UploadSdsResponseDto> uploadSection12And13And14(@ModelAttribute UploadRequestDto request);

    /* ================= POST ================= */
    @Operation(summary = "Add Section10 Item")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Data fetched successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/item", produces = "application/json")
    ResponseEntity<Section10ResponseDTO> addItem(@RequestBody Section10RequestDTO requestDTO);

    /* ================= DELETE ================= */
    @Operation(summary = "Delete Section10 Item")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Data fetched successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @DeleteMapping(value = "/item/{id}", produces = "application/json")
    ResponseEntity<String> deleteItem(@PathVariable Long id);
}