package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.SdsMaster;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.UploadProjection;

@Repository
public interface UploadRepository extends JpaRepository<SdsMaster, Long> {


    /* ================= DUPLICATE DETECTION ================= */
    @Query(value = """
        SELECT 
            id AS sdsId,
            product_identifier AS productIdentifier,
            current_version AS currentVersion
        FROM sds_master
        WHERE LOWER(product_identifier) = LOWER(:productIdentifier)
        """, nativeQuery = true)
    UploadProjection findDuplicateSds(
            @Param("productIdentifier") String productIdentifier
    );


    /* ================= INSERT SDS MASTER ================= */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_master
        (product_identifier, sds_number, manufacturer_info, current_version, status, created_by)
        VALUES
        (:productIdentifier, :sdsNumber, :manufacturerInfo, 1, 'Draft', :createdBy)
        """, nativeQuery = true)
    void insertSdsMaster(
            @Param("productIdentifier") String productIdentifier,
            @Param("sdsNumber") String sdsNumber,
            @Param("manufacturerInfo") String manufacturerInfo,
            @Param("createdBy") Long createdBy
    );


    /* ================= GET SDS ID ================= */
    @Query(value = """
        SELECT id
        FROM sds_master
        WHERE LOWER(product_identifier) = LOWER(:productIdentifier)
        ORDER BY id DESC
        LIMIT 1
        """, nativeQuery = true)
    Long getSdsId(@Param("productIdentifier") String productIdentifier);


    /* ================= INSERT VERSION ================= */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_version
        (sds_id, version_number, file_url, change_notes, uploaded_by)
        VALUES
        (:sdsId, :versionNumber, :fileUrl, :changeNotes, :uploadedBy)
        """, nativeQuery = true)
    void insertVersion(
            @Param("sdsId") Long sdsId,
            @Param("versionNumber") Integer versionNumber,
            @Param("fileUrl") String fileUrl,
            @Param("changeNotes") String changeNotes,
            @Param("uploadedBy") Long uploadedBy
    );


    /* ================= GET VERSION ID ================= */
    @Query(value = """
        SELECT id
        FROM sds_version
        WHERE sds_id = :sdsId
        ORDER BY id DESC
        LIMIT 1
        """, nativeQuery = true)
    Long getVersionId(@Param("sdsId") Long sdsId);


    /* ================= INSERT SECTION1 ================= */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_section1
        (sds_id, version_id, product_identifier, other_identification, sds_number,
         recommended_use, recommended_restrictions, manufacturer_info, source_type)
        VALUES
        (:sdsId, :versionId, :productIdentifier, :otherIdentification, :sdsNumber,
         :recommendedUse, :recommendedRestrictions, :manufacturerInfo, :sourceType)
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


    /* ================= SAVE OCR RESULT ================= */
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


    /* ================= DUPLICATE LOG ================= */
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


    /* ================= AUDIT LOG ================= */
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