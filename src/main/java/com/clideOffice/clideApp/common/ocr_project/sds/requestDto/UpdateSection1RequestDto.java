package com.clideOffice.clideApp.common.ocr_project.sds.requestDto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UpdateSection1RequestDto {

    @NotBlank(message = "Product Identifier is required")
    private String productIdentifier;

    private String otherIdentification;

    private String sdsNumber;

    private String recommendedUse;

    private String recommendedRestrictions;

    private String manufacturerInfo;

    private String sourceType;
}