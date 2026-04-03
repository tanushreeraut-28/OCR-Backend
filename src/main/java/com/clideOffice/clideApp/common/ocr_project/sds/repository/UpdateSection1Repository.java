package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.SdsSection1;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateSection1Repository extends JpaRepository<SdsSection1, Long> {

    /* ================= GET CURRENT VERSION ================= */

    @Query(value = """
        SELECT current_version 
        FROM sds_master 
        WHERE id = :sdsId
        """, nativeQuery = true)
    Integer getCurrentVersion(@Param("sdsId") Long sdsId);


    /* ================= GET VERSION ID ================= */

    @Query(value = """
        SELECT id 
        FROM sds_version 
        WHERE sds_id = :sdsId 
        AND version_number = :versionNumber
        ORDER BY id DESC 
        LIMIT 1
        """, nativeQuery = true)
    Long getVersionId(@Param("sdsId") Long sdsId,
                      @Param("versionNumber") Integer versionNumber);


    /* ================= UPDATE SECTION 1 ================= */

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE sds_section1
        SET
            product_identifier = :productIdentifier,
            other_identification = :otherIdentification,
            sds_number = :sdsNumber,
            recommended_use = :recommendedUse,
            recommended_restrictions = :recommendedRestrictions,
            manufacturer_info = :manufacturerInfo,
            source_type = :sourceType
        WHERE sds_id = :sdsId
        AND version_id = :versionId
        """, nativeQuery = true)
    int updateSection1(
            @Param("sdsId") Long sdsId,
            @Param("versionId") Long versionId,
            @Param("productIdentifier") String productIdentifier,
            @Param("otherIdentification") String otherIdentification,
            @Param("sdsNumber") String sdsNumber,
            @Param("recommendedUse") String recommendedUse,
            @Param("recommendedRestrictions") String recommendedRestrictions,
            @Param("manufacturerInfo") String manufacturerInfo,
            @Param("sourceType") String sourceType
    );


    /* ================= INSERT SECTION 1 ================= */

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_section1 (
            sds_id,
            version_id,
            product_identifier,
            other_identification,
            sds_number,
            recommended_use,
            recommended_restrictions,
            manufacturer_info,
            source_type
        ) VALUES (
            :sdsId,
            :versionId,
            :productIdentifier,
            :otherIdentification,
            :sdsNumber,
            :recommendedUse,
            :recommendedRestrictions,
            :manufacturerInfo,
            :sourceType
        )
        """, nativeQuery = true)
    int insertSection1(
            @Param("sdsId") Long sdsId,
            @Param("versionId") Long versionId,
            @Param("productIdentifier") String productIdentifier,
            @Param("otherIdentification") String otherIdentification,
            @Param("sdsNumber") String sdsNumber,
            @Param("recommendedUse") String recommendedUse,
            @Param("recommendedRestrictions") String recommendedRestrictions,
            @Param("manufacturerInfo") String manufacturerInfo,
            @Param("sourceType") String sourceType
    );


    /* ================= UPDATE MASTER FIELDS (FIXED) ================= */

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE sds_master
        SET 
            product_identifier = :productIdentifier,
            sds_number = :sdsNumber,
            manufacturer_info = :manufacturerInfo
        WHERE id = :sdsId
        """, nativeQuery = true)
    int updateSdsMasterFields(
            @Param("sdsId") Long sdsId,
            @Param("productIdentifier") String productIdentifier,
            @Param("sdsNumber") String sdsNumber,
            @Param("manufacturerInfo") String manufacturerInfo
    );


    /* ================= UPDATE MASTER TIMESTAMP ================= */

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE sds_master
        SET updated_at = CURRENT_TIMESTAMP
        WHERE id = :sdsId
        """, nativeQuery = true)
    int updateSdsMasterTimestamp(@Param("sdsId") Long sdsId);
}