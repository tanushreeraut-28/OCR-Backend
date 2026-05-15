package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section1.SdsMaster;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.UploadProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadSdsRepository extends JpaRepository<SdsMaster, Long> {

    /* =====================================================
       DUPLICATE DETECTION
    ===================================================== */
    @Query(value = """
        SELECT 
            id AS sdsId,
            product_identifier AS productIdentifier,
            current_version AS currentVersion
        FROM sds_master
        WHERE LOWER(product_identifier) = LOWER(:productIdentifier)
        AND LOWER(COALESCE(sds_number, '')) = LOWER(COALESCE(:sdsNumber, ''))
        """, nativeQuery = true)
    UploadProjection findDuplicateSds(
            @Param("productIdentifier") String productIdentifier,
            @Param("sdsNumber") String sdsNumber
    );

    /* =====================================================
       SDS MASTER
    ===================================================== */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_master
        (product_identifier, sds_number, manufacturer_info,
         current_version, status, created_by, created_at, updated_at)
        VALUES
        (:productIdentifier, :sdsNumber, :manufacturerInfo,
         1, 'Draft', :createdBy, NOW(), NOW())
        """, nativeQuery = true)
    void insertSdsMaster(
            @Param("productIdentifier") String productIdentifier,
            @Param("sdsNumber") String sdsNumber,
            @Param("manufacturerInfo") String manufacturerInfo,
            @Param("createdBy") Long createdBy
    );

    @Query(value = """
        SELECT id
        FROM sds_master
        WHERE LOWER(product_identifier) = LOWER(:productIdentifier)
        AND LOWER(COALESCE(sds_number, '')) = LOWER(COALESCE(:sdsNumber, ''))
        ORDER BY id DESC
        LIMIT 1
        """, nativeQuery = true)
    Long getSdsId(
            @Param("productIdentifier") String productIdentifier,
            @Param("sdsNumber") String sdsNumber
    );

    /* =====================================================
       VERSION
    ===================================================== */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_version
        (sds_id, version_number, file_name, file_type,
         file_data, change_notes, uploaded_by)
        VALUES
        (:sdsId, :versionNumber, :fileName, :fileType,
         :fileData, :changeNotes, :uploadedBy)
        """, nativeQuery = true)
    void insertVersion(
            @Param("sdsId") Long sdsId,
            @Param("versionNumber") Integer versionNumber,
            @Param("fileName") String fileName,
            @Param("fileType") String fileType,
            @Param("fileData") byte[] fileData,
            @Param("changeNotes") String changeNotes,
            @Param("uploadedBy") Long uploadedBy
    );

    @Query(value = """
        SELECT id
        FROM sds_version
        WHERE sds_id = :sdsId
        ORDER BY id DESC
        LIMIT 1
        """, nativeQuery = true)
    Long getVersionId(@Param("sdsId") Long sdsId);

    /* =====================================================
       SECTION 1
    ===================================================== */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_section1
        (sds_id, version_id, product_identifier,
         other_identification, sds_number,
         recommended_use, recommended_restrictions,
         manufacturer_info, source_type)
        VALUES
        (:sdsId, :versionId, :productIdentifier,
         :otherIdentification, :sdsNumber,
         :recommendedUse, :recommendedRestrictions,
         :manufacturerInfo, :sourceType)
        """, nativeQuery = true)
    void insertSection1(
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

    /* =====================================================
       SECTION 2 (HAZARD)
    ===================================================== */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_section2_hazard
        (sds_id, version_id, hazard_classification,
         signal_word, other_hazards,
         source_type, created_at, is_master)
        VALUES
        (:sdsId, :versionId, :hazardClassification,
         :signalWord, :otherHazards,
         :sourceType, NOW(), TRUE)
        """, nativeQuery = true)
    void insertSection2(
            @Param("sdsId") Long sdsId,
            @Param("versionId") Long versionId,
            @Param("hazardClassification") String hazardClassification,
            @Param("signalWord") String signalWord,
            @Param("otherHazards") String otherHazards,
            @Param("sourceType") String sourceType
    );

    @Query(value = """
        SELECT id
        FROM sds_section2_hazard
        WHERE sds_id = :sdsId
        AND version_id = :versionId
        ORDER BY id DESC
        LIMIT 1
        """, nativeQuery = true)
    Long getSection2Id(
            @Param("sdsId") Long sdsId,
            @Param("versionId") Long versionId
    );

    /* =====================================================
       SECTION 2 CHILD TABLES
    ===================================================== */

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_hazard_statement
        (hazard_id, code, description, is_master)
        VALUES
        (:hazardId, NULL, :text, TRUE)
        """, nativeQuery = true)
    void insertHazardStatements(
            @Param("hazardId") Long hazardId,
            @Param("text") String text
    );

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_precautionary_statement
        (hazard_id, code, description, is_master)
        VALUES
        (:hazardId, NULL, :text, TRUE)
        """, nativeQuery = true)
    void insertPrecautionaryStatements(
            @Param("hazardId") Long hazardId,
            @Param("text") String text
    );

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_hazard_pictogram
        (hazard_id, image_url, code)
        VALUES
        (:hazardId, :text, NULL)
        """, nativeQuery = true)
    void insertPictograms(
            @Param("hazardId") Long hazardId,
            @Param("text") String text
    );

    /* =====================================================
       OCR RESULT
    ===================================================== */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_ocr_result
        (sds_id, version_id, raw_text, confidence_score)
        VALUES
        (:sdsId, :versionId, :rawText, :confidenceScore)
        """, nativeQuery = true)
    void saveOcrResult(
            @Param("sdsId") Long sdsId,
            @Param("versionId") Long versionId,
            @Param("rawText") String rawText,
            @Param("confidenceScore") Double confidenceScore
    );

    /* =====================================================
       DUPLICATE LOG
    ===================================================== */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_duplicate_log
        (product_identifier, existing_sds_id, action_taken)
        VALUES
        (:productIdentifier, :existingSdsId, :actionTaken)
        """, nativeQuery = true)
    void logDuplicate(
            @Param("productIdentifier") String productIdentifier,
            @Param("existingSdsId") Long existingSdsId,
            @Param("actionTaken") String actionTaken
    );

    /* =====================================================
       TIMESTAMP UPDATE
    ===================================================== */
    @Modifying
    @Transactional
    @Query(value = """
        UPDATE sds_master
        SET updated_at = CURRENT_TIMESTAMP
        WHERE id = :sdsId
        """, nativeQuery = true)
    void updateTimestamp(@Param("sdsId") Long sdsId);

    /* =====================================================
       AUDIT LOG
    ===================================================== */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_audit_log
        (sds_id, action, action_by, notes)
        VALUES
        (:sdsId, :action, :actionBy, :notes)
        """, nativeQuery = true)
    void insertAuditLog(
            @Param("sdsId") Long sdsId,
            @Param("action") String action,
            @Param("actionBy") Long actionBy,
            @Param("notes") String notes
    );
}