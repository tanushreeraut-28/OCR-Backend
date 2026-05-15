package com.clideOffice.clideApp.common.ocr_project.sds.entity.section5;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sds_section5_firefighting")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section5Firefighting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sds_id", nullable = false)
    private Long sdsId;

    @Column(name = "suitable_media", columnDefinition = "TEXT")
    private String suitableMedia;

    @Column(name = "unsuitable_media", columnDefinition = "TEXT")
    private String unsuitableMedia;

    @Column(name = "special_hazards", columnDefinition = "TEXT")
    private String specialHazards;

    @Column(name = "advice", columnDefinition = "TEXT")
    private String advice;

    @Column(name = "protective_equipment", columnDefinition = "TEXT")
    private String protectiveEquipment;

    // BLOB
    @Lob
    @Column(name = "file_data", columnDefinition = "LONGBLOB")
    private byte[] fileData;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_hash", unique = true)
    private String fileHash;

    @Column(name = "is_active")
    private Boolean isActive = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isActive == null) isActive = true;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}