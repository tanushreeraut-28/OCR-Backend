package com.clideOffice.clideApp.common.ocr_project.sds.entity.section4;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "first_aid_special_treatment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FirstAidSpecialTreatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sds_id", nullable = false)
    private Long sdsId;

    @Column(name = "treatment", columnDefinition = "TEXT")
    private String treatment;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.isActive == null) this.isActive = true;
    }
}