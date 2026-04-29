package com.clideOffice.clideApp.common.ocr_project.sds.Section6.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section6.AccidentalReleaseMeasure;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section6PartialUpdateRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section6PartialUpdateResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section6ResponseDTO;

public interface Section6Service {

    // Get Section 6 data by SDS ID
    Section6ResponseDTO getBySdsId(Long sdsId);

    // PARTIAL UPDATE
    Section6PartialUpdateResponseDto partialUpdate(Section6PartialUpdateRequestDTO requestDTO);
}