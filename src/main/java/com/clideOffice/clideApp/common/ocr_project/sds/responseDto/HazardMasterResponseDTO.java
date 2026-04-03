package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HazardMasterResponseDTO {

    // ✅ Multiple values for dropdown
    private List<String> classifications;
    private List<String> signalWords;

    // ✅ Reused DTOs
    private List<HazardStatementDto> hazardStatements;
    private List<PrecautionaryStatementDto> precautionaryStatements;
}