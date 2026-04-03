package com.clideOffice.clideApp.common.ocr_project.sds.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sds_section2_hazard")
@Getter
@Setter
public class SdsSection2Hazard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sds_id")
    private SdsMaster sdsMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id")
    private SdsVersion version;

    @Column(name = "hazard_classification")
    private String hazardClassification;

    @Column(name = "signal_word")
    private String signalWord;

    @Column(name = "source_type")
    private String sourceType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ✅ NEW FIELD
    @Column(name = "is_master")
    private Boolean isMaster;

    @OneToMany(mappedBy = "hazard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HazardStatement> hazardStatements;

    @OneToMany(mappedBy = "hazard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrecautionaryStatement> precautionaryStatements;

    @OneToMany(mappedBy = "hazard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HazardPictogram> pictograms;
}