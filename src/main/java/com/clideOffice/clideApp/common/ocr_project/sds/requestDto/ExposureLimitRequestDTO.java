package com.clideOffice.clideApp.common.ocr_project.sds.requestDto;

import lombok.Data;

@Data
public class ExposureLimitRequestDTO {

    private String casNo;
    private String chemicalName;

    private String countryOrganization; // BOELV, MAK etc.
    private String limitType;           // TWA, STEL

    private String valueNotes;
    private String unit;
}