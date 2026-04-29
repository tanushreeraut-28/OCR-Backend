package com.clideOffice.clideApp.common.ocr_project.sds.entity.section2;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sds_precautionary_statement")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrecautionaryStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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