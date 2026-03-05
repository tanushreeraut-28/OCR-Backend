package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.Data;

@Data
public class UploadResponseDto {

    private String status;

    private Long sdsId;

    private Integer version;

    private String fileUrl;

    private String message;

}