package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientResponseDTO {

    private Long id;
    private String chemicalName;
    private String casNumber;
    private String ecNumber;
    private String concentration; // "25 - 50%"
    private String classification;
}