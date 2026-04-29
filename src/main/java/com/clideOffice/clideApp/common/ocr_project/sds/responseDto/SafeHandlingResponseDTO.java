package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SafeHandlingResponseDTO {

    private Long id;
    private Long sdsId;

    private String precautions;
    private String hygieneMeasures;
    private String protectiveMeasures;
    private String firePrevention;
}