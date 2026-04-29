package com.clideOffice.clideApp.common.ocr_project.sds.requestDto;

import lombok.Data;

@Data
public class Section6PartialUpdateRequestDTO {

    private Long id;          // record id
    private String field;     // which field to update
    private String value;     // new value
}