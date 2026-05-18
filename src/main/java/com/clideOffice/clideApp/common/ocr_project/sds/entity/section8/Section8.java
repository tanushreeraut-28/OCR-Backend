package com.clideOffice.clideApp.common.ocr_project.sds.entity.section8;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;
import java.util.List;

@Entity
@Table(name = "section8")
@Getter
@Setter
public class Section8 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sdsId;

    @OneToMany(mappedBy = "section8", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExposureLimit> exposureLimits;

    @OneToOne(mappedBy = "section8", cascade = CascadeType.ALL)
    private EngineeringControl engineeringControl;

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