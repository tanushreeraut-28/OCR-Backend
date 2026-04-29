package com.clideOffice.clideApp.common.ocr_project.sds.requestDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section5FirefightingRequestDTO {

    private Long sdsId;

    private String suitableMedia;

    private String unsuitableMedia;

    private String specialHazards;

    private String advice;

    private String protectiveEquipment;
}