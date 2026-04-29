package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section2.SdsSection2Hazard;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.HazardMasterProjection;

@Repository
public interface HandleHazardOcrRepository extends JpaRepository<SdsSection2Hazard, Long> {

	Optional<SdsSection2Hazard> findBySdsMaster_Id(Long sdsId);
	
    @Query("""
        SELECT 
            h.hazardClassification AS hazardClassification,
            h.signalWord AS signalWord,
            hs.code AS code,
            hs.description AS description
        FROM SdsSection2Hazard h
        LEFT JOIN h.hazardStatements hs
        WHERE h.isMaster = true
    """)
    List<HazardMasterProjection> findMasterData();
}