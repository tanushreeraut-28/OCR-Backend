package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SafeStorageResponseDTO {

    private Long id;
    private Long sdsId;
    private String technicalMeasures;
    private String storageConditions;
    private String incompatibleMaterials;
    private String packagingMaterials;
    private String storageTemperature;
    private String storageArea;
    private String ventilation;
    private String additionalInformation;

}