package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EngineeringControlResponseDTO {

    private Long id;
    private Long sdsId;
    private String description;
}