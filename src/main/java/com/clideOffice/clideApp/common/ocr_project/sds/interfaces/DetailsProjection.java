package com.clideOffice.clideApp.common.ocr_project.sds.interfaces;

import java.time.LocalDateTime;

public interface DetailsProjection {

    Long getSdsId();

    String getFileUrl();

    Integer getVersion();

    String getStatus();

    LocalDateTime getUploadedAt();

    Double getConfidenceScore();

    String getProductIdentifier();

    String getOtherIdentification();

    String getSdsNumber();

    String getRecommendedUse();

    String getRecommendedRestrictions();

    String getManufacturerInfo();

    String getSourceType();

}