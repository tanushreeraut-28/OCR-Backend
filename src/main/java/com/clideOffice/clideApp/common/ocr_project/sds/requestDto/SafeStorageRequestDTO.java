package com.clideOffice.clideApp.common.ocr_project.sds.requestDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SafeStorageRequestDTO {

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
