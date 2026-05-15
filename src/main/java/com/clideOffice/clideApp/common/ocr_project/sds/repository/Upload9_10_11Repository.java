package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section9.PhysicalChemicalProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Upload9_10_11Repository
        extends JpaRepository<PhysicalChemicalProperties, Long> {

    // ================= DUPLICATE CHECK =================
    @Query(value = """

            SELECT COUNT(*)
            FROM sds_physical_chemical_properties
            WHERE file_hash = :fileHash

            UNION ALL

            SELECT COUNT(*)
            FROM section10_items
            WHERE file_hash = :fileHash

            UNION ALL

            SELECT COUNT(*)
            FROM section11
            WHERE file_hash = :fileHash

            """, nativeQuery = true)

    List<Integer> duplicateCheck(@Param("fileHash") String fileHash);

    default Integer checkDuplicateFile(String fileHash) {

        List<Integer> counts = duplicateCheck(fileHash);

        return counts.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    // ================= SECTION 9 =================

    @Modifying
    @Query(value = """

        INSERT INTO sds_physical_chemical_properties
        (
            sds_id,
            physical_state,
            colour,
            odour,
            melting_point,
            boiling_point,
            flammability,
            explosion_limit,
            flash_point,
            auto_ignition_temperature,
            decomposition_temperature,
            ph,
            kinematic_viscosity,
            solubility,
            partition_coefficient,
            vapour_pressure,
            density,
            relative_vapour_density,
            particle_characteristics,
            particle_size,
            explosive_properties,
            oxidising_properties,
            evaporation_rate,
            viscosity,
            bulk_density,
            moisture,
            voc_content,
            other_information,
            file_data,
            file_name,
            file_type,
            file_hash,
            is_active
        )

        VALUES
        (
            :sdsId,
            :physicalState,
            :colour,
            :odour,
            :meltingPoint,
            :boilingPoint,
            :flammability,
            :explosionLimit,
            :flashPoint,
            :autoIgnitionTemperature,
            :decompositionTemperature,
            :ph,
            :kinematicViscosity,
            :solubility,
            :partitionCoefficient,
            :vapourPressure,
            :density,
            :relativeVapourDensity,
            :particleCharacteristics,
            :particleSize,
            :explosiveProperties,
            :oxidisingProperties,
            :evaporationRate,
            :viscosity,
            :bulkDensity,
            :moisture,
            :vocContent,
            :otherInformation,
            :fileData,
            :fileName,
            :fileType,
            :fileHash,
            TRUE
        )

        """, nativeQuery = true)

    void insertSection9Data(

            @Param("sdsId") Long sdsId,
            @Param("physicalState") String physicalState,
            @Param("colour") String colour,
            @Param("odour") String odour,
            @Param("meltingPoint") String meltingPoint,
            @Param("boilingPoint") String boilingPoint,
            @Param("flammability") String flammability,
            @Param("explosionLimit") String explosionLimit,
            @Param("flashPoint") String flashPoint,
            @Param("autoIgnitionTemperature") String autoIgnitionTemperature,
            @Param("decompositionTemperature") String decompositionTemperature,
            @Param("ph") String ph,
            @Param("kinematicViscosity") String kinematicViscosity,
            @Param("solubility") String solubility,
            @Param("partitionCoefficient") String partitionCoefficient,
            @Param("vapourPressure") String vapourPressure,
            @Param("density") String density,
            @Param("relativeVapourDensity") String relativeVapourDensity,
            @Param("particleCharacteristics") String particleCharacteristics,
            @Param("particleSize") String particleSize,
            @Param("explosiveProperties") String explosiveProperties,
            @Param("oxidisingProperties") String oxidisingProperties,
            @Param("evaporationRate") String evaporationRate,
            @Param("viscosity") String viscosity,
            @Param("bulkDensity") String bulkDensity,
            @Param("moisture") String moisture,
            @Param("vocContent") String vocContent,
            @Param("otherInformation") String otherInformation,
            @Param("fileData") byte[] fileData,
            @Param("fileName") String fileName,
            @Param("fileType") String fileType,
            @Param("fileHash") String fileHash
    );

    // ================= SECTION 10 =================
    @Modifying
    @Query(value = """

    INSERT INTO section10_items
    (
        sds_id,
        item_name,
        information,
        remarks,
        is_deleted,
        file_data,
        file_name,
        file_type,
        file_hash
    )

    VALUES
    (
        :sdsId,
        :itemName,
        :information,
        :remarks,
        :isDeleted,
        :fileData,
        :fileName,
        :fileType,
        :fileHash
    )
    """, nativeQuery = true)

    void insertSection10Data(
            @Param("sdsId") Long sdsId,
            @Param("itemName") String itemName,
            @Param("information") String information,
            @Param("remarks") String remarks,
            @Param("isDeleted") Boolean isDeleted,
            @Param("fileData") byte[] fileData,
            @Param("fileName") String fileName,
            @Param("fileType") String fileType,
            @Param("fileHash") String fileHash
    );

    // ================= SECTION 11 =================
    @Modifying
    @Query(value = """

    INSERT INTO section11
    (
        sds_id,
        hazard_classes,
        test_summary,
        toxicological_properties,
        routes_of_exposure,
        symptoms,
        file_data,
        file_name,
        file_type,
        file_hash
    )

    VALUES
    (:sdsId,
        :hazardClasses,
        :testSummary,
        :toxicologicalProperties,
        :routesOfExposure,
        :symptoms,
        :fileData,
        :fileName,
        :fileType,
        :fileHash
    )
    """, nativeQuery = true)

    void insertSection11Data(
            @Param("sdsId") Long sdsId,
            @Param("hazardClasses") String hazardClasses,
            @Param("testSummary") String testSummary,
            @Param("toxicologicalProperties") String toxicologicalProperties,
            @Param("routesOfExposure") String routesOfExposure,
            @Param("symptoms") String symptoms,
            @Param("fileData") byte[] fileData,
            @Param("fileName") String fileName,
            @Param("fileType") String fileType,
            @Param("fileHash") String fileHash
    );
}