package com.clideOffice.clideApp.common.ocr_project.sds.entity.section10;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "section10_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section10Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sdsId;

    private String itemName;

    @Column(columnDefinition = "TEXT")
    private String information;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private Boolean isDeleted = false;

    // ================= FILE STORAGE =================

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;

    private String fileName;

    private String fileType;

    private String fileHash;
}