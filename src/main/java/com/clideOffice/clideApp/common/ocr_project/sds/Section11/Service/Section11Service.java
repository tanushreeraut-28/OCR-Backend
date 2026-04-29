package com.clideOffice.clideApp.common.ocr_project.sds.Section11.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section11RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section11DeleteResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section11ResponseDTO;

public interface Section11Service {

    // POST
    Section11ResponseDTO addOrUpdate(Section11RequestDTO requestDTO);

    // GET
    Section11ResponseDTO getBySdsId(Long sdsId);

    // PATCH
    Section11ResponseDTO partialUpdate(Long sdsId, Section11RequestDTO dto);

    // DELETE
    Section11DeleteResponseDTO delete(Long sdsId);
}
