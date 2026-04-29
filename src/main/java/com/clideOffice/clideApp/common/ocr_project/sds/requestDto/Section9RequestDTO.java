package com.clideOffice.clideApp.common.ocr_project.sds.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section9RequestDTO {

    private Long sdsId;

    // 9.1
    private String physicalState;
    private String colour;
    private String odour;
    private String meltingPoint;
    private String boilingPoint;
    private String flammability;
    private String explosionLimit;
    private String flashPoint;
    private String autoIgnitionTemperature;
    private String decompositionTemperature;
    private String ph;
    private String kinematicViscosity;
    private String solubility;
    private String partitionCoefficient;

    private String vapourPressure;
    private String density;
    private String relativeVapourDensity;
    private String particleCharacteristics;
    private String particleSize;
    private String explosiveProperties;
    private String oxidisingProperties;
    private String evaporationRate;
    private String viscosity;
    private String bulkDensity;
    private String moisture;
    private String vocContent;

    // 9.2
    private String otherInformation;
}