package com.clideOffice.clideApp.common.ocr_project.sds.entity.section1;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "plants")
@Data
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_id")
    private Long plantId;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "plant_name")
    private String plantName;

}