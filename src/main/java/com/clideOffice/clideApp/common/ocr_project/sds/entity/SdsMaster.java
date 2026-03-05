package com.clideOffice.clideApp.common.ocr_project.sds.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sds_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SdsMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_identifier", nullable = false)
    private String productIdentifier;

    @Column(name = "sds_number")
    private String sdsNumber;

    @Column(name = "manufacturer_info")
    private String manufacturerInfo;

    @Column(name = "current_version")
    private Integer currentVersion;

    @Column(name = "status")
    private String status;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}