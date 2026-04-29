package com.clideOffice.clideApp.common.ocr_project.sds.entity.section8;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "engineering_control")
@Getter
@Setter
public class EngineeringControl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @OneToOne
    @JoinColumn(name = "section8_id")
    private Section8 section8;
}