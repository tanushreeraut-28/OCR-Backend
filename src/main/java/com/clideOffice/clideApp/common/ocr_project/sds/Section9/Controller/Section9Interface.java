package com.clideOffice.clideApp.common.ocr_project.sds.Section9.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section9RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section9ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/section9")
@Tag(name = "SDS Section 9 API", description = "Operations related to SDS Section 9")
public interface Section9Interface {

    /* ================= POST SECTION 9 ================= */
    @Operation(summary = "Add or Update Section 9", description = "Create or update Section 9 data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Saved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    ResponseEntity<Section9ResponseDTO> addOrUpdate(@RequestBody Section9RequestDTO requestDTO);

    /* ================= GET SECTION 9 ================= */
    @Operation(summary = "Get Section 9 by SDS ID", description = "Fetch physical and chemical properties using SDS ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{sdsId}")
    ResponseEntity<Section9ResponseDTO> getBySdsId(@PathVariable Long sdsId);

    /* ================= UPDATE SECTION 9 ================= */
    @Operation(summary = "Update Section 9", description = "Update existing Section 9 data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{id}")
    ResponseEntity<Section9ResponseDTO> update(
            @PathVariable Long id,
            @RequestBody Section9RequestDTO requestDTO
    );


}