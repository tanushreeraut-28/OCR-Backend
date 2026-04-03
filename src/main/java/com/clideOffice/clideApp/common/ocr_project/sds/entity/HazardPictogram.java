package com.clideOffice.clideApp.common.ocr_project.sds.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sds_hazard_pictogram")
@Getter
@Setter
public class HazardPictogram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hazard_id")
    private SdsSection2Hazard hazard;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "code") // optional (GHS01, GHS02, etc.)
    private String code;
}