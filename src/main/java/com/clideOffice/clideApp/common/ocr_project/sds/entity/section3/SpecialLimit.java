package com.clideOffice.clideApp.common.ocr_project.sds.entity.section3;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "special_limits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String substanceName;

    private String limitValue;

    private String hazardClass;

    private String remarks;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "sds_id", nullable = false)
    private Long sdsId;

    @Column(name = "hazard_phrase", columnDefinition = "TEXT")
    private String hazardPhrase;

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
