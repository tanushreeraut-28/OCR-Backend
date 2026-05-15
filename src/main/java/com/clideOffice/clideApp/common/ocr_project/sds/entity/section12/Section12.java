package com.clideOffice.clideApp.common.ocr_project.sds.entity.section12;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "section12")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section12 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sdsId;

    private String toxicityValue;
    private String toxicityNotes;

    private String persistenceValue;
    private String persistenceNotes;

    private String bioaccumulativeValue;
    private String bioaccumulativeNotes;

    private String mobilityValue;
    private String mobilityNotes;

    private String pbtValue;
    private String pbtNotes;

    private String endocrineValue;
    private String endocrineNotes;

    private String otherEffectsValue;
    private String otherEffectsNotes;

    @Column(length = 2000)
    private String additionalInfo;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(columnDefinition = "TEXT")
    private String rawText;

    @Column(columnDefinition = "TEXT")
    private String notes;

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