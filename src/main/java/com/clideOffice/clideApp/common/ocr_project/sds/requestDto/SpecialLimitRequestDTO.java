package com.clideOffice.clideApp.common.ocr_project.sds.requestDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialLimitRequestDTO {

    private Long sdsId;
    private String substanceName;
    private String limitValue;
    private String hazardClass;
    private String remarks;
}