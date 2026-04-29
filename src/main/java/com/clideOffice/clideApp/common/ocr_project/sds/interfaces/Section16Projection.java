package com.clideOffice.clideApp.common.ocr_project.sds.interfaces;

public interface Section16Projection {

    Long getId();
    Long getSdsId();

    String getIndicationOfChanges();
    String getAbbreviations();
    String getReferences();
    String getClassificationProcedure();
    String getHPhrases();
    String getTrainingAdvice();
    String getAdditionalInformation();

    String getDateOfIssue();
    String getDateOfRevision();
    String getVersion();
    String getPreparedBy();
    String getSdsNumber();
    String getDisclaimer();
}