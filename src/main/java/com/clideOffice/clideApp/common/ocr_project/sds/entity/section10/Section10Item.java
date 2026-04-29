package com.clideOffice.clideApp.common.ocr_project.sds.entity.section10;

import jakarta.persistence.*;

import lombok.*;

@Entity

@Table(name = "section10_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section10Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sdsId;

    // e.g. "10.1 Reactivity", "10.2 Chemical Stability"

    private String itemName;

    @Column(columnDefinition = "TEXT")
    private String information;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private Boolean isDeleted = false;

}
 