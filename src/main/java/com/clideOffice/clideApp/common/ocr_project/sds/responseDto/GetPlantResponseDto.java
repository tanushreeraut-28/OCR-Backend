package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPlantResponseDto {

    private Long plantId;
    private String plantName;

}