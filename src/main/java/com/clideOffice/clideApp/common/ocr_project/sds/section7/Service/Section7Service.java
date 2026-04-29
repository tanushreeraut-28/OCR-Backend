package com.clideOffice.clideApp.common.ocr_project.sds.section7.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SafeHandlingRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SafeStorageRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.requestDto.SpecificEndUseRequestDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SafeHandlingResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SafeStorageResponseDTO;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.SpecificEndUseResponseDTO;

public interface Section7Service {

    //  GET Safe Handling
    SafeHandlingResponseDTO getSafeHandling(Long sdsId);

    // ADD Safe Handling
    SafeHandlingResponseDTO addSafeHandling(SafeHandlingRequestDTO requestDTO);

    // UPDATE Safe Handling
    SafeHandlingResponseDTO updateSafeHandling(Long id, SafeHandlingRequestDTO requestDTO);

    // GET Safe Storage
    SafeStorageResponseDTO getStorageConditions(Long sdsId);

    //  ADD / UPDATE Safe Storage
    SafeStorageResponseDTO addOrUpdate(SafeStorageRequestDTO requestDTO);

    // GET Specific End Use
    SpecificEndUseResponseDTO getSpecificEndUse(Long sdsId);

    // EDIT Specific End Use
    SpecificEndUseResponseDTO addOrUpdateSpecificEndUse(Long sdsId, SpecificEndUseRequestDTO requestDTO);

}
