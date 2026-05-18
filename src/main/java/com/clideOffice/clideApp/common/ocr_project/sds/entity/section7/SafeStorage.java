package com.clideOffice.clideApp.common.ocr_project.sds.entity.section7;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "sds_safe_storage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SafeStorage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sdsId;

    @Column(columnDefinition = "TEXT")
    private String technicalMeasures;

    @Column(columnDefinition = "TEXT")
    private String storageConditions;

    @Column(columnDefinition = "TEXT")
    private String incompatibleMaterials;

    @Column(columnDefinition = "TEXT")
    private String packagingMaterials;

    @Column(columnDefinition = "TEXT")
    private String storageTemperature;

    @Column(columnDefinition = "TEXT")
    private String storageArea;

    @Column(columnDefinition = "TEXT")
    private String ventilation;

    @Column(columnDefinition = "TEXT")
    private String additionalInformation;

    // FILE

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;

    private String fileName;

    private String fileType;

    @Column(unique = true)
    private String fileHash;

    private Boolean isActive;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}