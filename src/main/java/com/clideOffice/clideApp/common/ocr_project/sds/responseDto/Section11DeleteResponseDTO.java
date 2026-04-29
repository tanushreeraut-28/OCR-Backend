package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Section11DeleteResponseDTO {

    private String message;
    private Long sdsId;
}