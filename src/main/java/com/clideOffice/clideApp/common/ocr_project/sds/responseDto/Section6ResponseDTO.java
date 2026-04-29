// ===== Response DTO =====
package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Section6ResponseDTO {

    private Long id;
    private Long sdsId;
    private String personalPrecautions;
    private String environmentalPrecautions;
    private String containmentMethods;
    private String referenceSections;
}