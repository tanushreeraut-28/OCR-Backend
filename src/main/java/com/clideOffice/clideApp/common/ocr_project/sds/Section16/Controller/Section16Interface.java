package com.clideOffice.clideApp.common.ocr_project.sds.Section16.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section16RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section16ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/section16")
@Tag(name = "SDS Section 16 API", description = "Operations related to SDS Section 16 - Other Information")
public interface Section16Interface {

    /* ================= POST ================= */
    @Operation(summary = "Add or Update Section16 (Other Information)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Data saved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(value = "/other-information", produces = "application/json")
    ResponseEntity<Section16ResponseDTO> addItem(@RequestBody Section16RequestDTO requestDTO); // UPSERT

    /* ================= GET ================= */
    @Operation(summary = "Get Section16 by SDS ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Data fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping(value = "/other-information/{sdsId}", produces = "application/json")
    ResponseEntity<Section16ResponseDTO> getBySdsId(@PathVariable Long sdsId); // FETCH

    /* ================= DELETE ================= */
    @Operation(summary = "Delete Section16 by SDS ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping(value = "/other-information/{sdsId}", produces = "application/json")
    ResponseEntity<String> deleteBySdsId(@PathVariable Long sdsId); // DELETE
}