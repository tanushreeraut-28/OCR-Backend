package com.clideOffice.clideApp.common.ocr_project.sds.requestDto;

import lombok.Data;

@Data
public class Section16RequestDTO {

    private Long sdsId; // SDS reference ID

    private String indicationOfChanges; // 16.1
    private String abbreviations; // 16.2
    private String references; // 16.3
    private String classificationProcedure; // 16.4
    private String hPhrases; // 16.5
    private String trainingAdvice; // 16.6
    private String additionalInformation; // 16.7

    private String dateOfIssue; // Document info
    private String dateOfRevision;
    private String version;
    private String preparedBy;
    private String sdsNumber;
    private String disclaimer;
}