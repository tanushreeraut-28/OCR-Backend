package com.clideOffice.clideApp.common.ocr_project.sds.Section16.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section16RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section16ResponseDTO;

public interface Section16Service {

    Section16ResponseDTO addItem(Section16RequestDTO dto);

    Section16ResponseDTO getBySdsId(Long sdsId);

    void deleteBySdsId(Long sdsId);
}