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

    /* 16.1 */
    @Column(columnDefinition = "TEXT")
    private String indicationOfChanges;

    /* 16.2 */
    @Column(columnDefinition = "TEXT")
    private String abbreviations;

    /* 16.3 */
    @Column(name = "reference_text", columnDefinition = "TEXT")
    private String references;

    /* 16.4 */
    @Column(columnDefinition = "TEXT")
    private String classificationProcedure;

    /* 16.5 */
    @Column(columnDefinition = "TEXT")
    private String hPhrases;

    /* 16.6 */
    @Column(columnDefinition = "TEXT")
    private String trainingAdvice;

    /* 16.7 */
    @Column(columnDefinition = "TEXT")
    private String additionalInformation;

    /* Document Info */
    private String dateOfIssue;
    private String dateOfRevision;
    private String version;
    private String preparedBy;
    private String sdsNumber;

    @Column(columnDefinition = "TEXT")
    private String disclaimer;
}