package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.SdsMaster;

import jakarta.transaction.Transactional;

@Repository
public interface ConfirmOCRRepository extends JpaRepository<SdsMaster, Long> {

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE sds_ocr_result so
        JOIN sds_master sm ON sm.id = so.sds_id
        JOIN sds_version sv ON sv.sds_id = sm.id 
            AND sv.version_number = sm.current_version
        SET so.ocr_status = 'CONFIRMED'
        WHERE sm.id = :sdsId
        AND sv.id = so.version_id
        """, nativeQuery = true)
    void confirmOcr(Long sdsId);

}