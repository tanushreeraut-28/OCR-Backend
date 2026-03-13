package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OcrExtractedDataResponseDto {

    private Long sdsId;
    private Long versionId;

    private String product;
    private String otherName;
    private String sdsNumber;
    private String use;
    private String restriction;
    private String manufacturer;

    private String sourceType;
    private String message;
}