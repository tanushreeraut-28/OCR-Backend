package com.clideOffice.clideApp.common.ocr_project.sds.requestDto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVersionRequestDto {

    private MultipartFile file;

    private String changeNotes;

    private Long uploadedBy;

}