package com.clideOffice.clideApp.common.ocr_project.sds.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;  // added
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.CreateVersionRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UpdateSection1RequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.ConfirmOCRResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.CreateVersionResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DashboardSummaryResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.DetailsResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.OcrExtractedDataResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.RemoveUserResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetPlantResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.GetServiceResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UpdateSection1ResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RequestMapping("/api") 
@Tag(name = "SDS API", description = "Operations related to SDS Upload")
public interface SdsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /* ================= SDS Upload API ================= */
    @Operation(summary = "Upload SDS File", description = "Upload SDS document")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Upload Success"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/sds/upload", consumes = "multipart/form-data", produces = "application/json")
    ResponseEntity<UploadResponseDto> uploadSds(@ModelAttribute UploadRequestDto request);

    /* ================= Details API ================= */
    @Operation(summary = "Details of SDS File", description = "Details of SDS document")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Details fetch Successfully"),@ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping(value = "/sds/{sdsId}", produces = "application/json")
    ResponseEntity<DetailsResponseDto> getSdsDetails(@PathVariable Long sdsId);

    /* ================= Update Section 1 After OCR Review ================= */
    @Operation(summary = "Update SDS Section1 after OCR Review", description = "User edits OCR extracted fields and saves corrected SDS Section 1 data")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "SDS updated successfully"), @ApiResponse(responseCode = "404", description = "SDS not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PutMapping(value = "/sds/{sdsId}/section1", consumes = "application/json", produces = "application/json")
    ResponseEntity<UpdateSection1ResponseDto> updateSection1(@PathVariable Long sdsId,@RequestBody UpdateSection1RequestDto request);

    /* ================= Create Version API ================= */
    @Operation(summary = "Create New SDS Version", description = "Upload new version of an existing SDS document")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Version created successfully"), @ApiResponse(responseCode = "404", description = "SDS not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/sds/{sdsId}/versions", consumes = "multipart/form-data", produces = "application/json")
    ResponseEntity<CreateVersionResponseDto> createVersion(@PathVariable Long sdsId, @ModelAttribute CreateVersionRequestDto request);

    /* ================= OCR Extracted Data API ================= */
    @Operation(summary = "Get OCR Extracted Data",description = "Fetch OCR extracted data for OCR Review Screen")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "OCR data fetched successfully"), @ApiResponse(responseCode = "404", description = "SDS not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping(value = "/sds/{sdsId}/ocr-data", produces = "application/json")
    ResponseEntity<OcrExtractedDataResponseDto> getOcrExtractedData(@PathVariable Long sdsId);

    /* ================= Confirm OCR Review API ================= */
    @Operation(summary = "Confirm OCR Extracted Data",description = "User confirms OCR extracted data after review")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "OCR confirmed successfully"),@ApiResponse(responseCode = "404", description = "SDS not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping(value = "/sds/{sdsId}/confirm-ocr", produces = "application/json")
    ResponseEntity<ConfirmOCRResponseDto> confirmOCR(@PathVariable Long sdsId);

    /* ================= Dashboard Summary API ================= */
    @Operation(summary = "Get SDS Dashboard Summary",description = "Fetch counts for Total SDS, Pending Review, and Duplicate SDS")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Dashboard summary fetched successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping(value = "/sds/dashboard-summary", produces = "application/json")
    ResponseEntity<DashboardSummaryResponseDto> getDashboardSummary();

    /* ================= Get All Services ================= */
    @Operation(summary = "Get All Services", description = "Fetch all available services")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Services fetched successfully"),  @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping(value = "/sds/services", produces = "application/json")
    ResponseEntity<List<GetServiceResponseDto>> getAllServices();

    /* ================= Get Plants / Projects by Service ================= */
    @Operation(summary = "Get Plants/Projects by Service",description = "Fetch plants/projects linked with selected service")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Plants fetched successfully"), @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping(value = "/sds/plants", produces = "application/json")
    ResponseEntity<List<GetPlantResponseDto>> getPlantsByService(@RequestParam Long serviceId);

    /* ================= Remove User API ================= */
    @Operation(summary = "Remove User", description = "Remove user from system by soft deleting the user")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "User removed successfully"),@ApiResponse(responseCode = "404", description = "User not found"),@ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PutMapping(value = "/sds/{userId}/remove", produces = "application/json")
    ResponseEntity<RemoveUserResponseDto> removeUser(@PathVariable Integer userId);
}