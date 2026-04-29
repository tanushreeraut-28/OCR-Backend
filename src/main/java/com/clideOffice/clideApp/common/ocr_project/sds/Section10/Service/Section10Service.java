package com.clideOffice.clideApp.common.ocr_project.sds.Section10.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section10RequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section10ResponseDTO;

public interface Section10Service {

    // POST (Add Item)
    Section10ResponseDTO addItem(Section10RequestDTO requestDTO);

    // DELETE (Soft Delete)
    void deleteItem(Long id);
}