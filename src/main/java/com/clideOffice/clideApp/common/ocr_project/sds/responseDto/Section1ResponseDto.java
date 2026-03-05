package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section1ResponseDto {

    private String productIdentifier;

    private String otherIdentification;

    private String sdsNumber;

    private String recommendedUse;

    private String recommendedRestrictions;

    private String manufacturerInfo;

    private String sourceType;

}