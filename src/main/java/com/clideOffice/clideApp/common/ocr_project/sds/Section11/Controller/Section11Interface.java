package com.clideOffice.clideApp.common.ocr_project.sds.Section11.Controller;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section11RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section11DeleteResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section11ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/section11")
@Tag(name = "SDS Section 11 API", description = "Operations related to SDS Section 11")
public interface Section11Interface {

    /* ================= POST ================= */
    @Operation(summary = "Add or Update Section11")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Data saved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(value = "/item", produces = "application/json")
    ResponseEntity<Section11ResponseDTO> addItem(@RequestBody Section11RequestDTO requestDTO);

    /* ================= GET ================= */
    @Operation(summary = "Get Section11 by SDS ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Data fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping(value = "/{sdsId}", produces = "application/json")
    ResponseEntity<Section11ResponseDTO> getBySdsId(@PathVariable Long sdsId);

    /* ================= PATCH ================= */
    @Operation(summary = "Partially Update Section11")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PatchMapping(value = "/{sdsId}", produces = "application/json")
    ResponseEntity<Section11ResponseDTO> partialUpdate(
            @PathVariable Long sdsId,
            @RequestBody Section11RequestDTO requestDTO);


    /* ================= DELETE ================= */
    @Operation(summary = "Delete Section11 by SDS ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping(value = "/{sdsId}", produces = "application/json")
    ResponseEntity<Section11DeleteResponseDTO> delete(@PathVariable Long sdsId);
}
