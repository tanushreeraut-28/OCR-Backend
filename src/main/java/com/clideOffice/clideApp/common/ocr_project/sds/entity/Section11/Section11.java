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
    private Long id;   // MySQL auto increment

    @Column(nullable = false, unique = true)
    private Long sdsId;  // One Section11 per SDS

    // 11.1
    @Column(columnDefinition = "TEXT")
    private String hazardClasses;

    // 11.1.1
    @Column(columnDefinition = "TEXT")
    private String testSummary;

    // 11.1.2
    @Column(columnDefinition = "TEXT")
    private String toxicologicalProperties;

    // 11.1.3
    @Column(columnDefinition = "TEXT")
    private String routesOfExposure;

    // 11.1.4
    @Column(columnDefinition = "TEXT")
    private String symptoms;

    // 11.1.5
    @Column(columnDefinition = "TEXT")
    private String delayedEffects;

    // 11.1.6
    @Column(columnDefinition = "TEXT")
    private String interactiveEffects;

    // 11.1.7
    @Column(columnDefinition = "TEXT")
    private String absenceOfData;

    // 11.2
    @Column(columnDefinition = "TEXT")
    private String otherHazards;
}