package com.clideOffice.clideApp.common.ocr_project.sds.service;

import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.RemoveUserResponseDto;

public interface RemoveUserService {

    RemoveUserResponseDto removeUser(Integer userId);

}