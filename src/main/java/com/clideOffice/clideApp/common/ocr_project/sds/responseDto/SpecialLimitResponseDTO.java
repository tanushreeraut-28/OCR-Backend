package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialLimitResponseDTO {

    private Long id;
    private Long sdsId;
    private String substanceName;
    private String limitValue;
    private String hazardClass;
    private String remarks;
}