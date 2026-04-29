package com.clideOffice.clideApp.common.ocr_project.sds.entity.section3;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sds_id", nullable = false)
    private Long sdsId;

    @Column(name = "chemical_name", nullable = false)
    private String chemicalName;

    @Column(name = "cas_number")
    private String casNumber;

    @Column(name = "ec_number")
    private String ecNumber;

    @Column(name = "concentration_min")
    private Double concentrationMin;

    @Column(name = "concentration_max")
    private Double concentrationMax;

    private String classification;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isActive == null) this.isActive = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}