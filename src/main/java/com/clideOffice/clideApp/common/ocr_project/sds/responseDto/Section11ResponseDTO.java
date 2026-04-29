package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Section11ResponseDTO {

    private Long sdsId;

    private String hazardClasses;
    private String testSummary;
    private String toxicologicalProperties;
    private String routesOfExposure;
    private String symptoms;
    private String delayedEffects;
    private String interactiveEffects;
    private String absenceOfData;
    private String otherHazards;
}