package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section5.Section5Firefighting;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Upload5And6Repository extends JpaRepository<Section5Firefighting, Long> {

    // ================= DUPLICATE CHECK =================

    @Query(value = """
            SELECT COUNT(*)
            FROM sds_section5_firefighting
            WHERE file_hash = :uploadedFileHash
            """, nativeQuery = true)
    int checkDuplicateFile(String uploadedFileHash);

    // ================= SECTION 5 =================

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_section5_firefighting
        (
         sds_id,
         suitable_media,
         unsuitable_media,
         special_hazards,
         advice,
         protective_equipment,
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
         :suitableMedia,
         :unsuitableMedia,
         :specialHazards,
         :firefighterAdvice,
         :protectiveEquipment,
         :uploadedFileData,
         :uploadedFileName,
         :uploadedFileType,
         :uploadedFileHash,
         TRUE,
         NOW(),
         NOW()
        )
        """, nativeQuery = true)
    void insertSection5Data(
            Long sdsId,
            String suitableMedia,
            String unsuitableMedia,
            String specialHazards,
            String firefighterAdvice,
            String protectiveEquipment,
            byte[] uploadedFileData,
            String uploadedFileName,
            String uploadedFileType,
            String uploadedFileHash
    );

    // ================= SECTION 6 =================

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO accidental_release_measure
        (
         sds_id,
         personal_precautions,
         environmental_precautions,
         containment_methods,
         reference_sections,
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
         :personalPrecautions,
         :environmentalPrecautions,
         :containmentMethods,
         :referenceSections,
         :uploadedFileData,
         :uploadedFileName,
         :uploadedFileType,
         :uploadedFileHash,
         TRUE,
         NOW(),
         NOW()
        )
        """, nativeQuery = true)
    void insertSection6Data(
            Long sdsId,
            String personalPrecautions,
            String environmentalPrecautions,
            String containmentMethods,
            String referenceSections,
            byte[] uploadedFileData,
            String uploadedFileName,
            String uploadedFileType,
            String uploadedFileHash
    );
}