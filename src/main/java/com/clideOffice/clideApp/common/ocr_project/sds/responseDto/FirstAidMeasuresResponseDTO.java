package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FirstAidMeasuresResponseDTO {

    private Long sdsId;
    private List<FirstAidMeasureDTO> measures;
}