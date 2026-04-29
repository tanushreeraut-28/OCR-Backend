package com.clideOffice.clideApp.common.ocr_project.sds.requestDto;

import lombok.Data;

@Data
public class Section11RequestDTO {

    private Long sdsId;

    // 11.1
    private String hazardClasses;

    // 11.1.1
    private String testSummary;

    // 11.1.2
    private String toxicologicalProperties;

    // 11.1.3
    private String routesOfExposure;

    // 11.1.4
    private String symptoms;

    // 11.1.5
    private String delayedEffects;

    // 11.1.6
    private String interactiveEffects;

    // 11.1.7
    private String absenceOfData;

    // 11.2
    private String otherHazards;
}