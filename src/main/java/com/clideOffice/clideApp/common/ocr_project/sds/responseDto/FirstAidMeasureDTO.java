package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FirstAidMeasureDTO {

    private Long id;
    private String emergencyOverview;
    private String generalAdvice;
    private String afterInhalation;
    private String afterSkinContact;
    private String afterEyeContact;
    private String afterSwallowing;
    private String immediateMedicalAttention;
}