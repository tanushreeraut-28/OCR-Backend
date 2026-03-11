package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveUserResponseDto {

    private Long userId;

    private String message;

}