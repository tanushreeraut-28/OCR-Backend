package com.clideOffice.clideApp.common.ocr_project.sds.interfaces;

public interface OcrExtractedDataProjection {

    Long getSdsId();

    Long getVersionId();

    String getProductIdentifier();

    String getOtherIdentification();

    String getSdsNumber();

    String getRecommendedUse();

    String getRecommendedRestrictions();

    String getManufacturerInfo();

    String getSourceType();

    String getRawText();

    Double getConfidenceScore();
}