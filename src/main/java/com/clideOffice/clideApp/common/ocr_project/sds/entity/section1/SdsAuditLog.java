package com.clideOffice.clideApp.common.ocr_project.sds.entity.section1;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "sds_audit_log")
@Data
public class SdsAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sds_id")
    private Long sdsId;

    private String action;

    @Column(name = "action_by")
    private Long actionBy;

    @Column(name = "action_date")
    private LocalDateTime actionDate;

    private String notes;
}
