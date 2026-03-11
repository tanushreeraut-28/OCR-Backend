package com.clideOffice.clideApp.common.ocr_project.sds.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "sds_section1")
@Data
public class SdsSection1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sds_id")
    private SdsMaster sdsMaster;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id")
    private SdsVersion version;

    @Column(name = "product_identifier", nullable = false)
    private String productIdentifier;

    @Column(name = "other_identification")
    private String otherIdentification;

    @Column(name = "sds_number")
    private String sdsNumber;

    @Column(name = "recommended_use")
    private String recommendedUse;

    @Column(name = "recommended_restrictions")
    private String recommendedRestrictions;

    @Column(name = "manufacturer_info")
    private String manufacturerInfo;

    @Column(name = "source_type")
    private String sourceType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}