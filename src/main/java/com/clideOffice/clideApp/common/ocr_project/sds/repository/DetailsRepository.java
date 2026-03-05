package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.SdsMaster;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.DetailsProjection;

@Repository
public interface DetailsRepository extends JpaRepository<SdsMaster, Long> {

    @Query(value = """
        SELECT
            sm.id                         AS sdsId,
            sv.file_url                   AS fileUrl,
            sv.version_number             AS version,
            sm.status                     AS status,
            sv.uploaded_at                AS uploadedAt,
            ocr.confidence_score          AS confidenceScore,
            s1.product_identifier         AS productIdentifier,
            s1.other_identification       AS otherIdentification,
            s1.sds_number                 AS sdsNumber,
            s1.recommended_use            AS recommendedUse,
            s1.recommended_restrictions   AS recommendedRestrictions,
            s1.manufacturer_info          AS manufacturerInfo,
            s1.source_type                AS sourceType
        FROM sds_master sm
        LEFT JOIN sds_version sv 
               ON sm.id = sv.sds_id
        LEFT JOIN sds_section1 s1 
               ON sm.id = s1.sds_id
        LEFT JOIN sds_ocr_result ocr 
               ON sm.id = ocr.sds_id
        WHERE sm.id = :sdsId
        ORDER BY sv.version_number DESC
        LIMIT 1
        """, nativeQuery = true)
    DetailsProjection getSdsDetails(Long sdsId);

}