package com.clideOffice.clideApp.common.ocr_project.sds.requestDto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadRequestDto {

    private MultipartFile file;

}