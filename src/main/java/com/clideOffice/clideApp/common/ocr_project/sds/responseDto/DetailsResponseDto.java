package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailsResponseDto {

    private Long sdsId;

    private String fileUrl;

    private Integer version;

    private String status;

    private LocalDateTime uploadedAt;

    private String ocrStatus;

    private Double confidenceScore;

    private Section1ResponseDto section1;

}
