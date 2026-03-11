package com.clideOffice.clideApp.common.ocr_project.sds.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clideOffice.clideApp.common.ocr_project.sds.repository.RemoveUserRepository;
import com.clideOffice.clideApp.common.ocr_project.sds.responseDto.RemoveUserResponseDto;
import com.clideOffice.clideApp.common.ocr_project.sds.service.RemoveUserService;

@Service
public class RemoveUserServiceImpl implements RemoveUserService {

    @Autowired
    private RemoveUserRepository removeUserRepository;

    @Override
    public RemoveUserResponseDto removeUser(Integer userId) {

        int updatedRows = removeUserRepository.removeUser(userId);

        if (updatedRows == 0) {
            throw new RuntimeException("User not found");
        }

        RemoveUserResponseDto response = new RemoveUserResponseDto();
        response.setUserId(Long.valueOf(userId));
        response.setMessage("User removed successfully");

        return response;
    }
}