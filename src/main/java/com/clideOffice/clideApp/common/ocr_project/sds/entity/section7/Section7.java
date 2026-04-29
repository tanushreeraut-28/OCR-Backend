package com.clideOffice.clideApp.common.ocr_project.sds.entity.section7;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sds_section7")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section7 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sdsId;

    @Column(columnDefinition = "TEXT")
    private String description; // Header text
}