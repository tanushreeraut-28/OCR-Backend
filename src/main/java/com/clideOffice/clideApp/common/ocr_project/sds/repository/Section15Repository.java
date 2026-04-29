package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section15.RegulatoryInformation;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.Section15Projection;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface Section15Repository extends JpaRepository<RegulatoryInformation, Long> {

    /* 🔹 FETCH using Projection */
    @Query("""
        SELECT 
            r.id AS id,
            r.sdsId AS sdsId,
            r.safetyHealthEnvironmentalRegulations AS safetyHealthEnvironmentalRegulations,
            r.directive2012_18_EU AS directive2012_18_EU,
            r.reachAnnexXVII AS reachAnnexXVII,
            r.directive2011_65_EU AS directive2011_65_EU,
            r.regulationEU2019_1148 AS regulationEU2019_1148,
            r.regulationEC273_2004 AS regulationEC273_2004,
            r.regulationEC111_2005 AS regulationEC111_2005,
            r.chemicalSafetyAssessment AS chemicalSafetyAssessment
        FROM RegulatoryInformation r
        WHERE r.sdsId = :sdsId
    """)
    Optional<Section15Projection> findBySdsId(@Param("sdsId") Long sdsId);

    /* 🔹 FETCH ENTITY (for UPSERT) */
    @Query("SELECT r FROM RegulatoryInformation r WHERE r.sdsId = :sdsId")
    Optional<RegulatoryInformation> findEntityBySdsId(@Param("sdsId") Long sdsId);

    /* 🔹 DELETE */
    @Modifying
    @Query("DELETE FROM RegulatoryInformation r WHERE r.sdsId = :sdsId")
    void deleteBySdsId(@Param("sdsId") Long sdsId);
}