package com.clideOffice.clideApp.common.ocr_project.sds.requestDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SafeHandlingRequestDTO {

    private Long sdsId;

    private String precautions;
    private String hygieneMeasures;
    private String protectiveMeasures;
    private String firePrevention;
}