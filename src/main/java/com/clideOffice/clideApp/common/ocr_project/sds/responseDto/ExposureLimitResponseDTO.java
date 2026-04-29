package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExposureLimitResponseDTO {

    private Long id;
    private Long sdsId;

    private String casNo;
    private String chemicalName;

    private String countryOrganization;
    private String limitType;

    private String valueNotes;
    private String unit;
}