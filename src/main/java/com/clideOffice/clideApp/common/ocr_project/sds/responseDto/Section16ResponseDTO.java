package com.clideOffice.clideApp.common.ocr_project.sds.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Section16ResponseDTO {

    private Long id;
    private Long sdsId;

    private String indicationOfChanges;
    private String abbreviations;
    private String references;
    private String classificationProcedure;
    private String hPhrases;
    private String trainingAdvice;
    private String additionalInformation;

    private String dateOfIssue;
    private String dateOfRevision;
    private String version;
    private String preparedBy;
    private String sdsNumber;
    private String disclaimer;
}