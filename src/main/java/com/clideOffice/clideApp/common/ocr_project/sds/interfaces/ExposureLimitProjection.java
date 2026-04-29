package com.clideOffice.clideApp.common.ocr_project.sds.interfaces;

public interface ExposureLimitProjection {

    Long getId();
    String getCasNo();
    String getChemicalName();
    String getCountryOrganization();
    String getLimitType();
    String getValueNotes();
    String getUnit();

    Section8Info getSection8();

    interface Section8Info {
        Long getSdsId();
    }
}