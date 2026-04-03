package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HandleHazardOcrResponseDto {

    private String hazardClassification;
    private String signalWord;

    private List<HazardStatementDto> hazardStatements;
    private List<PrecautionaryStatementDto> precautionaryStatements;
    private List<HazardPictogramDto> pictograms;

    private String message; // optional (success / preview info)
}