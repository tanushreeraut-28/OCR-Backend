package com.clideOffice.clideApp.common.ocr_project.sds.entity.section13;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "section13")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section13 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sdsId;

    @Column(columnDefinition = "TEXT")
    private String wasteTreatmentMethods;

    @Column(columnDefinition = "TEXT")
    private String productDisposal;

    @Column(columnDefinition = "TEXT")
    private String packagingDisposal;

    @Column(columnDefinition = "TEXT")
    private String propertiesAffectingDisposal;

    @Column(columnDefinition = "TEXT")
    private String sewageDisposal;

    @Column(columnDefinition = "TEXT")
    private String specialPrecautions;

    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

    private Boolean isOcr;

    @Column(name = "is_draft")
    private Boolean isDraft;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "is_valid")
    private Boolean isValid;

    // ===== FILE STORAGE =====

    @Lob
    @Column(name = "file_data", columnDefinition = "LONGBLOB")
    private byte[] fileData;

    private String fileName;

    private String fileType;

    @Column(unique = true)
    private String fileHash;

    private Boolean isActive = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}