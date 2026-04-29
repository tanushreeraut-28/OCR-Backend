package com.clideOffice.clideApp.common.ocr_project.sds.Section6.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section6PartialUpdateRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section6PartialUpdateResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section6ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/accidentalrelease")
@Tag(name = "SDS Section 6 API", description = "Operations related to SDS Section 6")
public interface Section6Interface {

    /* ================= GET SECTION 6 ================= */
    @Operation(summary = "Get Section 6 by SDS ID", description = "Fetch Section 6 data using SDS ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{sdsId}")
    ResponseEntity<Section6ResponseDTO> getBySdsId(@PathVariable Long sdsId);

    /* ================= PARTIAL UPDATE ================= */
    @Operation(summary = "Partial Update Section 6", description = "Update specific field")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Record not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PatchMapping("/{id}")
    ResponseEntity<Section6PartialUpdateResponseDto> partialUpdate(
            @PathVariable Long id,
            @RequestBody Section6PartialUpdateRequestDTO requestDTO
    );
}