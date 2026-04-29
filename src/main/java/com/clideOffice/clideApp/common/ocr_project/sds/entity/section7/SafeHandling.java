package com.clideOffice.clideApp.common.ocr_project.sds.entity.section7;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sds_safe_handling")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SafeHandling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sdsId;

    @Column(columnDefinition = "TEXT")
    private String precautions;

    @Column(columnDefinition = "TEXT")
    private String hygieneMeasures;

    @Column(columnDefinition = "TEXT")
    private String protectiveMeasures;

    @Column(columnDefinition = "TEXT")
    private String firePrevention;

}