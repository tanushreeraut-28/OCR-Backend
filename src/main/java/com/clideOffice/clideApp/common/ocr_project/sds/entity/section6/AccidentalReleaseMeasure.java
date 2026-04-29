package com.clideOffice.clideApp.common.ocr_project.sds.entity.section6;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accidental_release_measure")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccidentalReleaseMeasure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sdsId;

    @Column(columnDefinition = "TEXT")
    private String personalPrecautions;

    @Column(columnDefinition = "TEXT")
    private String environmentalPrecautions;

    @Column(columnDefinition = "TEXT")
    private String containmentMethods;

    @Column(columnDefinition = "TEXT")
    private String referenceSections;

    private Boolean isActive = true;

}
