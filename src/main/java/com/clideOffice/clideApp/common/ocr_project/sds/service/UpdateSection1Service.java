package com.clideOffice.clideApp.common.ocr_project.sds.service;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UpdateSection1RequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UpdateSection1ResponseDto;

public interface UpdateSection1Service {

    UpdateSection1ResponseDto updateSection1(Long sdsId, UpdateSection1RequestDto request);

}