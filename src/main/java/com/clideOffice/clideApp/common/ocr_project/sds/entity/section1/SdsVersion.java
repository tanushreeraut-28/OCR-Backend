package com.clideOffice.clideApp.common.ocr_project.sds.entity.section1;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sds_version")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SdsVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sds_id")
    private SdsMaster sdsMaster;

    @Column(name = "version_number")
    private Integer versionNumber;

    // =========================
    // OLD URL COLUMN
    // =========================
    @Column(name = "file_url")
    private String fileUrl;

    // =========================
    // NEW BLOB STORAGE COLUMNS
    // =========================
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Lob
    @Column(name = "file_data", columnDefinition = "LONGBLOB")
    private byte[] fileData;

    // =========================
    // OTHER FIELDS
    // =========================
    @Column(name = "change_notes")
    private String changeNotes;

    @Column(name = "uploaded_by")
    private Long uploadedBy;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    public SdsVersion(Long id) {
        this.id = id;
    }
}