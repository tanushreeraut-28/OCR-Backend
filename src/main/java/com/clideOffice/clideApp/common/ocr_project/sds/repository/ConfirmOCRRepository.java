package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section1.SdsMaster;

import jakarta.transaction.Transactional;

@Repository
public interface ConfirmOCRRepository extends JpaRepository<SdsMaster, Long> {

	@Modifying
	@Transactional
	@Query(value = """
	    UPDATE sds_ocr_result
	    SET ocr_status = 'CONFIRMED'
	    WHERE sds_id = :sdsId
	    AND version_id = (
	        SELECT id FROM sds_version 
	        WHERE sds_id = :sdsId 
	        ORDER BY version_number DESC LIMIT 1
	    )
	    """, nativeQuery = true)
	int confirmOcr(@Param("sdsId") Long sdsId);
	
}