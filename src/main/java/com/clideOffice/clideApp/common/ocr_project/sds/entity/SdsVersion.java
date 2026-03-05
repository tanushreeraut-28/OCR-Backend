package com.clideOffice.clideApp.common.ocr_project.sds.entity;

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

    @Column(name = "sds_id")
    private Long sdsId;

    @Column(name = "version_number")
    private Integer versionNumber;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "change_notes")
    private String changeNotes;

    @Column(name = "uploaded_by")
    private Long uploadedBy;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

}
