package com.clideOffice.clideApp.common.ocr_project.sds.entity.section15;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "regulatory_information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegulatoryInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sdsId;

    /* 15.1 - Safety, health & environmental regulations */
    @Column(columnDefinition = "TEXT")
    private String safetyHealthEnvironmentalRegulations;

    /* 15.2 - Directive 2012/18/EU */
    @Column(columnDefinition = "TEXT")
    private String directive2012_18_EU;

    /* 15.3 - REACH Annex XVII */
    @Column(columnDefinition = "TEXT")
    private String reachAnnexXVII;

    /* 15.4 - Directive 2011/65/EU */
    @Column(columnDefinition = "TEXT")
    private String directive2011_65_EU;

    /* 15.5 - Regulation (EU) 2019/1148 */
    @Column(columnDefinition = "TEXT")
    private String regulationEU2019_1148;

    /* 15.6 - Regulation (EC) No 273/2004 */
    @Column(columnDefinition = "TEXT")
    private String regulationEC273_2004;

    /* 15.7 - Regulation (EC) No 111/2005 */
    @Column(columnDefinition = "TEXT")
    private String regulationEC111_2005;

    /* Chemical Safety Assessment */
    @Column(columnDefinition = "TEXT")
    private String chemicalSafetyAssessment;
}