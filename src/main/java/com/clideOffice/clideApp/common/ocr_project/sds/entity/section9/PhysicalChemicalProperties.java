package com.clideOffice.clideApp.common.ocr_project.sds.entity.section9;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sds_physical_chemical_properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhysicalChemicalProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sdsId;

    // 🔹 9.1 Basic Physical Properties

    private String physicalState;
    private String colour;
    private String odour;
    private String meltingPoint;
    private String boilingPoint;
    private String flammability;
    private String explosionLimit;
    private String flashPoint;
    private String autoIgnitionTemperature;
    private String decompositionTemperature;
    private String ph;
    private String kinematicViscosity;
    private String solubility;
    private String partitionCoefficient;

    // 🔹 Additional Physical & Chemical Properties

    private String vapourPressure;
    private String density;
    private String relativeVapourDensity;
    private String particleCharacteristics;
    private String particleSize;
    private String explosiveProperties;
    private String oxidisingProperties;
    private String evaporationRate;
    private String viscosity;
    private String bulkDensity;
    private String moisture;
    private String vocContent;

    // 🔹 9.2 Other Information
    @Column(columnDefinition = "TEXT")
    private String otherInformation;
}