package com.clideOffice.clideApp.common.ocr_project.sds.Section15.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section15RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.UploadRequestDto;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section15ResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.UploadSdsResponseDto;

public interface Section15Service {

    Section15ResponseDTO addItem(Section15RequestDTO requestDTO);

    Section15ResponseDTO getBySdsId(Long sdsId);

    void deleteBySdsId(Long sdsId);

    // SDS Upload API 15, 16
    UploadSdsResponseDto uploadSection15And16(UploadRequestDto request);

}