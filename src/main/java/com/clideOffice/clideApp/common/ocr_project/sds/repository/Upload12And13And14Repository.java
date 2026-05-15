package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section12.Section12;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Upload12And13And14Repository
        extends JpaRepository<Section12, Long> {

    // Duplicate check

    @Query(value = """

            SELECT
            (
                (SELECT COUNT(*) FROM section12 WHERE file_hash = :fileHash)
                +
                (SELECT COUNT(*) FROM section13 WHERE file_hash = :fileHash)
                +
                (SELECT COUNT(*) FROM section14 WHERE file_hash = :fileHash)
            )

            """, nativeQuery = true)
    int checkDuplicateFile(String fileHash);

    // Generate SDS ID

    @Query(value = """

            SELECT
            COALESCE(MAX(sds_id),0)+1
            FROM
            (
                SELECT sds_id FROM section12
                UNION ALL
                SELECT sds_id FROM section13
                UNION ALL
                SELECT sds_id FROM section14
            ) a

            """, nativeQuery = true)
    Long generateNextSdsId();

    // Section 12

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = """

        INSERT INTO section12
        (
            sds_id,
            toxicity_value,
            persistence_value,
            bioaccumulative_value,
            mobility_value,
            pbt_value,
            endocrine_value,
            other_effects_value,
            additional_info,
            raw_text,
            file_data,
            file_name,
            file_type,
            file_hash,
            is_active,
            created_at,
            updated_at
        )

        VALUES
        (
            :sdsId,
            :toxicity,
            :persistence,
            :bioaccumulative,
            :mobility,
            :pbt,
            :endocrine,
            :otherEffects,
            :additionalInfo,
            :rawText,
            :fileData,
            :fileName,
            :fileType,
            :fileHash,
            TRUE,
            NOW(),
            NOW()
        )

        """, nativeQuery = true)
    void insertSection12Data(
            Long sdsId,
            String toxicity,
            String persistence,
            String bioaccumulative,
            String mobility,
            String pbt,
            String endocrine,
            String otherEffects,
            String additionalInfo,
            String rawText,
            byte[] fileData,
            String fileName,
            String fileType,
            String fileHash
    );

    // Section 13

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = """

        INSERT INTO section13
        (
            sds_id,
            waste_treatment_methods,
            product_disposal,
            packaging_disposal,
            properties_affecting_disposal,
            sewage_disposal,
            special_precautions,
            additional_info,
            file_data,
            file_name,
            file_type,
            file_hash,
            is_active,
            created_at,
            updated_at
        )

        VALUES
        (
            :sdsId,
            :wasteTreatment,
            :productDisposal,
            :packagingDisposal,
            :propertiesDisposal,
            :sewageDisposal,
            :specialPrecautions,
            :additionalInfo,
            :fileData,
            :fileName,
            :fileType,
            :fileHash,
            TRUE,
            NOW(),
            NOW()
        )

        """, nativeQuery = true)
    void insertSection13Data(
            Long sdsId,
            String wasteTreatment,
            String productDisposal,
            String packagingDisposal,
            String propertiesDisposal,
            String sewageDisposal,
            String specialPrecautions,
            String additionalInfo,
            byte[] fileData,
            String fileName,
            String fileType,
            String fileHash
    );

    // Section 14

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = """

        INSERT INTO section14
        (
            sds_id,
            un_number,
            proper_shipping_name,
            hazard_class,
            packing_group,
            environmental_hazards,
            special_precautions,
            maritime_transport,
            additional_info,
            file_data,
            file_name,
            file_type,
            file_hash,
            is_active,
            created_at,
            updated_at
        )

        VALUES
        (
            :sdsId,
            :unNumber,
            :shippingName,
            :hazardClass,
            :packingGroup,
            :environmentalHazards,
            :specialPrecautions,
            :maritimeTransport,
            :additionalInfo,
            :fileData,
            :fileName,
            :fileType,
            :fileHash,
            TRUE,
            NOW(),
            NOW()
        )

        """, nativeQuery = true)
    void insertSection14Data(
            Long sdsId,
            String unNumber,
            String shippingName,
            String hazardClass,
            String packingGroup,
            String environmentalHazards,
            String specialPrecautions,
            String maritimeTransport,
            String additionalInfo,
            byte[] fileData,
            String fileName,
            String fileType,
            String fileHash
    );
}