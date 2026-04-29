package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Section15ResponseDTO {

    private Long id;
    private Long sdsId;

    private String safetyHealthEnvironmentalRegulations;
    private String directive2012_18_EU;
    private String reachAnnexXVII;
    private String directive2011_65_EU;
    private String regulationEU2019_1148;
    private String regulationEC273_2004;
    private String regulationEC111_2005;
    private String chemicalSafetyAssessment;
}