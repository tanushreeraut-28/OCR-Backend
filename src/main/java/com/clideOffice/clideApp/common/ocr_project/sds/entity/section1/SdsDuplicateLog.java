package com.clideOffice.clideApp.common.ocr_project.sds.entity.section1;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "sds_duplicate_log")
@Data
public class SdsDuplicateLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_identifier")
    private String productIdentifier;

    @Column(name = "existing_sds_id")
    private Long existingSdsId;

    @Column(name = "detected_at")
    private LocalDateTime detectedAt;

    @Column(name = "action_taken")
    private String actionTaken;
}
