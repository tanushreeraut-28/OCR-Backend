package com.clideOffice.clideApp.common.ocr_project.sds.interfaces;

public interface DashboardSummaryProjection {

    Long getTotalSds();

    Long getPendingReview();

    Long getDuplicates();
}