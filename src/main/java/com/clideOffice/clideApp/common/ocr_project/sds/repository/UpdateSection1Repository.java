package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.SdsSection1;

@Repository
public interface UpdateSection1Repository extends JpaRepository<SdsSection1, Long> {


    /* ================= CHECK SDS EXISTS ================= */

    @Query(value = """
        SELECT COUNT(1)
        FROM sds_master
        WHERE id = :sdsId
        """, nativeQuery = true)
    Integer checkSdsExists(@Param("sdsId") Long sdsId);



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
        """, nativeQuery = true)
    void updateSection1(
            @Param("sdsId") Long sdsId,
            @Param("productIdentifier") String productIdentifier,
            @Param("otherIdentification") String otherIdentification,
            @Param("sdsNumber") String sdsNumber,
            @Param("recommendedUse") String recommendedUse,
            @Param("recommendedRestrictions") String recommendedRestrictions,
            @Param("manufacturerInfo") String manufacturerInfo,
            @Param("sourceType") String sourceType
    );



    /* ================= UPDATE MASTER TIMESTAMP ================= */

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE sds_master
        SET updated_at = CURRENT_TIMESTAMP
        WHERE id = :sdsId
        """, nativeQuery = true)
    void updateMasterTimestamp(@Param("sdsId") Long sdsId);

}