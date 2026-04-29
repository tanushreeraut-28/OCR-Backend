package com.clideOffice.clideApp.common.ocr_project.sds.entity.section8;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "exposure_limit")
@Getter
@Setter
public class ExposureLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String casNo;
    private String chemicalName;

    private String countryOrganization;
    private String limitType;

    private String valueNotes;
    private String unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section8_id")
    private Section8 section8;
}