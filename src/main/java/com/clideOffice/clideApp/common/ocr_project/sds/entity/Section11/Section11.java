package com.clideOffice.clideApp.common.ocr_project.sds.entity.Section11;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "section11")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section11 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long sdsId;

    @Column(columnDefinition = "TEXT")
    private String hazardClasses;

    @Column(columnDefinition = "TEXT")
    private String testSummary;

    @Column(columnDefinition = "TEXT")
    private String toxicologicalProperties;

    @Column(columnDefinition = "TEXT")
    private String routesOfExposure;

    @Column(columnDefinition = "TEXT")
    private String symptoms;

    @Column(columnDefinition = "TEXT")
    private String delayedEffects;

    @Column(columnDefinition = "TEXT")
    private String interactiveEffects;

    @Column(columnDefinition = "TEXT")
    private String absenceOfData;

    @Column(columnDefinition = "TEXT")
    private String otherHazards;

    // ================= FILE STORAGE =================

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;

    private String fileName;

    private String fileType;

    private String fileHash;
}