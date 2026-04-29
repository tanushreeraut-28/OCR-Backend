package com.clideOffice.clideApp.common.ocr_project.sds.requestDto;

import lombok.Data;

@Data
public class Section15RequestDTO {

    private Long sdsId;

    /* 15.1 */
    private String safetyHealthEnvironmentalRegulations;

    /* 15.2 */
    private String directive2012_18_EU;

    /* 15.3 */
    private String reachAnnexXVII;

    /* 15.4 */
    private String directive2011_65_EU;

    /* 15.5 */
    private String regulationEU2019_1148;

    /* 15.6 */
    private String regulationEC273_2004;

    /* 15.7 */
    private String regulationEC111_2005;

    /* Chemical Safety Assessment */
    private String chemicalSafetyAssessment;
}