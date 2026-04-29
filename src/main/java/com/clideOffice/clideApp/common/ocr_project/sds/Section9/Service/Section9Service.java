package com.clideOffice.clideApp.common.ocr_project.sds.Section9.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section9RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section9ResponseDTO;

public interface Section9Service {

    // POST
    Section9ResponseDTO addOrUpdate(Section9RequestDTO requestDTO);

    /* ================= GET SECTION 9 ================= */
    Section9ResponseDTO getBySdsId(Long sdsId);

    // Update
    Section9ResponseDTO update(Long id, Section9RequestDTO requestDTO);
}