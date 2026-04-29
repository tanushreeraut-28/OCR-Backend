package com.clideOffice.clideApp.common.ocr_project.sds.Section15.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section15RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section15ResponseDTO;

public interface Section15Service {

    Section15ResponseDTO addItem(Section15RequestDTO requestDTO);

    Section15ResponseDTO getBySdsId(Long sdsId);

    void deleteBySdsId(Long sdsId);
}