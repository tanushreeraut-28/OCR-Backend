package com.clideOffice.clideApp.common.ocr_project.sds.requestDto;

import lombok.Data;

@Data
public class UpdateIngredientRequestDTO {

    private String chemicalName;
    private String casNumber;
    private String ecNumber;
    private Double concentrationMin;
    private Double concentrationMax;
    private String classification;
}