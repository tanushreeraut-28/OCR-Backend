package com.clideOffice.clideApp.common.ocr_project.sds.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "sds_ocr_result")
@Data
public class SdsOcrResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sds_id")
    private Long sdsId;

    @Column(name = "version_id")
    private Long versionId;

    @Column(name = "raw_text", columnDefinition = "LONGTEXT")
    private String rawText;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    

	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "assigned_at")
	private LocalDateTime assignedAt;
	
	@Column(name = "notes")
	private String notes;
}
