package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OcrExtractedDataSection2ResponseDto {

    private Long sdsId;
    private Long versionId;

    // Dropdown fields
    private String hazardClassification;
    private String signalWord;

    // Textarea fields (IMPORTANT: keep as String, not List)
    private String hazardStatements;
    private String precautionaryStatements;

    // Optional (UI "+" button handles pictograms separately)
    private String pictogramCodes; // e.g. "GHS02,GHS07"

    private String otherHazards;

    private String message;
}