package com.clideOffice.clideApp.common.ocr_project.sds.entity.section5;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sds_section5_firefighting")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section5Firefighting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sds_id", nullable = false)
    private Long sdsId;

    @Column(name = "suitable_media")
    private String suitableMedia;

    @Column(name = "unsuitable_media")
    private String unsuitableMedia;

    @Column(name = "special_hazards")
    private String specialHazards;

    @Column(name = "advice")
    private String advice;

    @Column(name = "protective_equipment")
    private String protectiveEquipment;

    @Column(name = "is_active")
    private Boolean isActive = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}