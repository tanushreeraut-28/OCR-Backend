package com.clideOffice.clideApp.common.ocr_project.sds.entity.section2;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sds_hazard_statement")
@Getter
@Setter
public class HazardStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK to Section-2
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hazard_id")
    private SdsSection2Hazard hazard;

    @Column(name = "code")
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // ✅ NEW FIELD
    @Column(name = "is_master")
    private Boolean isMaster;
}