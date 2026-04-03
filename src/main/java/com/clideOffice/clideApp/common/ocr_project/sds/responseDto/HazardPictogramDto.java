package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HazardPictogramDto {
    private String code;
    private String imageUrl;
}