package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.SdsMaster;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.CreateVersionProjection;

@Repository
public interface CreateVersionRepository extends JpaRepository<SdsMaster, Long> {

    /* Get current version */
    @Query(value = """
        SELECT 
            sm.id AS sdsId,
            sm.current_version AS currentVersion
        FROM sds_master sm
        WHERE sm.id = :sdsId
        """, nativeQuery = true)
    CreateVersionProjection getCurrentVersion(Long sdsId);


    /* Insert new SDS version */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO sds_version
        (sds_id, version_number, file_url, change_notes, uploaded_by)
        VALUES
        (:sdsId, :versionNumber, :fileUrl, :changeNotes, :uploadedBy)
        """, nativeQuery = true)
    void insertNewVersion(
            Long sdsId,
            Integer versionNumber,
            String fileUrl,
            String changeNotes,
            Long uploadedBy
    );


    /* Update master current version */
    @Modifying
    @Transactional
    @Query(value = """
        UPDATE sds_master
        SET current_version = :versionNumber,
            updated_at = NOW()
        WHERE id = :sdsId
        """, nativeQuery = true)
    void updateCurrentVersion(
            Long sdsId,
            Integer versionNumber
    );

}