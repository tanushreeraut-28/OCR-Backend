package com.clideOffice.clideApp.common.ocr_project.sds.interfaces;

public interface Section11Projection {

    Long getSdsId();

    String getHazardClasses();
    String getTestSummary();
    String getToxicologicalProperties();
    String getRoutesOfExposure();
    String getSymptoms();
    String getDelayedEffects();
    String getInteractiveEffects();
    String getAbsenceOfData();
    String getOtherHazards();
}