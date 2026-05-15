// ================= SECTION 16 ENTITY =================

package com.clideOffice.clideApp.common.ocr_project.sds.entity.section16;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "other_information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtherInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sdsId;

    @Column(columnDefinition = "TEXT")
    private String indicationOfChanges;

    @Column(columnDefinition = "TEXT")
    private String abbreviations;

    @Column(name = "reference_text", columnDefinition = "TEXT")
    private String references;

    @Column(columnDefinition = "TEXT")
    private String classificationProcedure;

    @Column(columnDefinition = "TEXT")
    private String hPhrases;

    @Column(columnDefinition = "TEXT")
    private String trainingAdvice;

    @Column(columnDefinition = "TEXT")
    private String additionalInformation;

    private String dateOfIssue;

    private String dateOfRevision;

    private String version;

    private String preparedBy;

    private String sdsNumber;

    @Column(columnDefinition = "TEXT")
    private String disclaimer;

    // ================= FILE STORE =================

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;

    private String fileName;

    private String fileType;

    @Column(unique = true, length = 500)
    private String fileHash;
}