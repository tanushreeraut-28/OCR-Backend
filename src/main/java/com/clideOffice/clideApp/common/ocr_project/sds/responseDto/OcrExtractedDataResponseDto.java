package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OcrExtractedDataResponseDto {

    private Long sdsId;

    private Long versionId;

    /* ===== Section 1 Fields (from sds_section1 table) ===== */

    private String productIdentifier;

    private String otherIdentification;

    private String sdsNumber;

    private String recommendedUse;

    private String recommendedRestrictions;

    private String manufacturerInfo;

    private String sourceType; // OCR or MANUAL


    /* ===== OCR Metadata (from sds_ocr_result table) ===== */

    private String rawText;

    private Double confidenceScore;


    /* ===== Response Info ===== */

    private String message;

}