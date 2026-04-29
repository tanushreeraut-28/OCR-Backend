package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section5FirefightingResponseDTO {

    private Long id;

    private Long sdsId;

    private String suitableMedia;

    private String unsuitableMedia;

    private String specialHazards;

    private String advice;

    private String protectiveEquipment;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String message;
}