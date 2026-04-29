package com.clideOffice.clideApp.common.ocr_project.sds.entity.section4;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "first_aid_measures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FirstAidMeasures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sds_id", nullable = false)
    private Long sdsId;

    @Column(name = "emergency_overview", columnDefinition = "TEXT")
    private String emergencyOverview;

    @Column(name = "general_advice", columnDefinition = "TEXT")
    private String generalAdvice;

    @Column(name = "after_inhalation", columnDefinition = "TEXT")
    private String afterInhalation;

    @Column(name = "after_skin_contact", columnDefinition = "TEXT")
    private String afterSkinContact;

    @Column(name = "after_eye_contact", columnDefinition = "TEXT")
    private String afterEyeContact;

    @Column(name = "after_swallowing", columnDefinition = "TEXT")
    private String afterSwallowing;

    @Column(name = "immediate_medical_attention", columnDefinition = "TEXT")
    private String immediateMedicalAttention;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.updatedAt = LocalDateTime.now();
        if (this.isActive == null) this.isActive = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}