package com.clideOffice.clideApp.common.ocr_project.sds.upload.controller;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadSdsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@Validated
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/upload")
@Tag(name = "SDS Upload API", description = "Operations related to SDS Upload")
public interface UploadInterface {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /* ================= SDS Upload API 1 & 2 ================= */
    @Operation(summary = "Upload SDS File", description = "Upload SDS document")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Upload Success"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/sds", consumes = "multipart/form-data", produces = "application/json")
    ResponseEntity<UploadSdsResponseDto> uploadSds(@ModelAttribute UploadRequestDto request);

    /* ================= SDS Upload API 5 & 6 ================= */
    @Operation(summary = "Upload SDS File for section 5 and 6", description = "Upload SDS document")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Upload Success"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/sds-5-6", consumes = "multipart/form-data", produces = "application/json")
    ResponseEntity<UploadSdsResponseDto> uploadSdsSection5And6(@ModelAttribute UploadRequestDto request);

    /* ================= SDS Upload API 9 & 10 & 11 ================ */
    @Operation(summary = "Upload SDS File for Section 9 10 11", description = "Upload SDS document for section 9, 10 and 11")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Upload Success"), @ApiResponse(responseCode = "400", description = "OCR Failed / Invalid Request"), @ApiResponse(responseCode = "409", description = "Duplicate File"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/sds-9-10-11", consumes = "multipart/form-data", produces = "application/json")
    ResponseEntity<UploadSdsResponseDto> uploadSection9And10And11(@ModelAttribute UploadRequestDto request);

}
