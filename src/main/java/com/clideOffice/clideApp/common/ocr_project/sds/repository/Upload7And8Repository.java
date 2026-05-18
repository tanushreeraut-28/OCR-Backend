package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section7.SafeHandling;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface Upload7And8Repository extends JpaRepository<SafeHandling, Long> {

    // ================= DUPLICATE CHECK =================
    @Query(value = """

            SELECT
            (
                (SELECT COUNT(*) FROM sds_safe_handling WHERE file_hash = :fileHash)
                +
                (SELECT COUNT(*) FROM sds_safe_storage WHERE file_hash = :fileHash)
                +
                (SELECT COUNT(*) FROM sds_specific_end_use WHERE file_hash = :fileHash)
            )

            """, nativeQuery = true)
    int checkDuplicateFile(String fileHash);

    // ================= GENERATE SDS ID =================

    @Query(value = """

            SELECT COALESCE(MAX(sds_id),0)+1
            FROM
            (
                SELECT sds_id FROM sds_safe_handling
                UNION ALL
                SELECT sds_id FROM sds_safe_storage
                UNION ALL
                SELECT sds_id FROM sds_specific_end_use
            ) a

            """, nativeQuery = true)
    Long generateNextSdsId();

    // ================= SECTION 7 =================

    @Modifying
    @Transactional
    @Query(value = """

        INSERT INTO sds_safe_handling
        (
            sds_id,
            precautions,
            hygiene_measures,
            protective_measures,
            fire_prevention,
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
            :precautions,
            :hygieneMeasures,
            :protectiveMeasures,
            :firePrevention,
            :fileData,
            :fileName,
            :fileType,
            :fileHash,
            TRUE,
            NOW(),
            NOW()
        )

        """, nativeQuery = true)
    void insertSafeHandlingData(
            Long sdsId,
            String precautions,
            String hygieneMeasures,
            String protectiveMeasures,
            String firePrevention,
            byte[] fileData,
            String fileName,
            String fileType,
            String fileHash
    );

    @Modifying
    @Transactional
    @Query(value = """

        INSERT INTO sds_safe_storage
        (
            sds_id,
            technical_measures,
            storage_conditions,
            incompatible_materials,
            packaging_materials,
            storage_temperature,
            storage_area,
            ventilation,
            additional_information,
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
            :technicalMeasures,
            :storageConditions,
            :incompatibleMaterials,
            :packagingMaterials,
            :storageTemperature,
            :storageArea,
            :ventilation,
            :additionalInformation,
            :fileData,
            :fileName,
            :fileType,
            :fileHash,
            TRUE,
            NOW(),
            NOW()
        )

        """, nativeQuery = true)
    void insertSafeStorageData(
            Long sdsId,
            String technicalMeasures,
            String storageConditions,
            String incompatibleMaterials,
            String packagingMaterials,
            String storageTemperature,
            String storageArea,
            String ventilation,
            String additionalInformation,
            byte[] fileData,
            String fileName,
            String fileType,
            String fileHash
    );

    @Modifying
    @Transactional
    @Query(value = """

        INSERT INTO sds_specific_end_use
        (
            sds_id,
            specific_end_use,
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
            :specificEndUse,
            :fileData,
            :fileName,
            :fileType,
            :fileHash,
            TRUE,
            NOW(),
            NOW()
        )

        """, nativeQuery = true)
    void insertSpecificEndUseData(
            Long sdsId,
            String specificEndUse,
            byte[] fileData,
            String fileName,
            String fileType,
            String fileHash
    );
}