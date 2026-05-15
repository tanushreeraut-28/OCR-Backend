package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.Data;

@Data
public class UploadSdsResponseDto {

    private String status;

    private Long sdsId;

    private Integer version;

    private String message;

}