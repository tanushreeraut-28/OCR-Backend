package com.clideOffice.clideApp.common.ocr_project.sds.entity.section14;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "section14")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section14 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sdsId;

    private String unNumber;

    private String properShippingName;

    private String hazardClass;

    private String packingGroup;

    private String environmentalHazards;

    private String specialPrecautions;

    private String maritimeTransport;

    @Column(length = 1000)
    private String additionalInfo;

    private Boolean isOcr;

    private Boolean isVerified;

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