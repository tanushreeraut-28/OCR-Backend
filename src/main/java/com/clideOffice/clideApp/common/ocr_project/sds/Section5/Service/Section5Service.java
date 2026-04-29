package com.clideOffice.clideApp.common.ocr_project.sds.Section5.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.Section5FirefightingRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.Section5FirefightingResponseDTO;

public interface Section5Service {

    // SAVE / UPDATE FIREFIGHTING
    Section5FirefightingResponseDTO saveOrUpdateFirefighting(Section5FirefightingRequestDTO requestDTO);

    // GET FIREFIGHTING BY SDS ID
    Section5FirefightingResponseDTO getFirefightingBySdsId(Long sdsId);
}
