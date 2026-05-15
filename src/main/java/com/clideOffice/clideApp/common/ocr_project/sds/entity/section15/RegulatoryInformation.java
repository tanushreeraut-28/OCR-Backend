// ================= SECTION 15 ENTITY =================

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

    @Column(name = "sds_id")
    private Long sdsId;

    @Column(
            name = "safety_health_environmental_regulations",
            columnDefinition = "TEXT"
    )
    private String safetyHealthEnvironmentalRegulations;

    @Column(
            name = "directive2012_18_eu",
            columnDefinition = "TEXT"
    )
    private String directive2012_18_EU;

    @Column(
            name = "reach_annex_xvii",
            columnDefinition = "TEXT"
    )
    private String reachAnnexXVII;

    @Column(
            name = "directive2011_65_eu",
            columnDefinition = "TEXT"
    )
    private String directive2011_65_EU;

    @Column(
            name = "regulation_eu2019_1148",
            columnDefinition = "TEXT"
    )
    private String regulationEU2019_1148;

    @Column(
            name = "regulation_ec273_2004",
            columnDefinition = "TEXT"
    )
    private String regulationEC273_2004;

    @Column(
            name = "regulation_ec111_2005",
            columnDefinition = "TEXT"
    )
    private String regulationEC111_2005;

    @Column(
            name = "chemical_safety_assessment",
            columnDefinition = "TEXT"
    )
    private String chemicalSafetyAssessment;

    // ================= FILE STORE =================

    @Lob
    @Column(
            name = "file_data",
            columnDefinition = "LONGBLOB"
    )
    private byte[] fileData;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(
            name = "file_hash",
            unique = true,
            length = 500
    )
    private String fileHash;

    // ================= COMMON =================

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;

    @Column(name = "updated_at")
    private java.sql.Timestamp updatedAt;
}