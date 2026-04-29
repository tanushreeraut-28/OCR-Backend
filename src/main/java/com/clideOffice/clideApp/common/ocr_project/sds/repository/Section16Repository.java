package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section16.OtherInformation;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.Section16Projection;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface Section16Repository extends JpaRepository<OtherInformation, Long> {

    @Query("""
        SELECT 
            o.id AS id,
            o.sdsId AS sdsId,
            o.indicationOfChanges AS indicationOfChanges,
            o.abbreviations AS abbreviations,
            o.references AS references,
            o.classificationProcedure AS classificationProcedure,
            o.hPhrases AS hPhrases,
            o.trainingAdvice AS trainingAdvice,
            o.additionalInformation AS additionalInformation,
            o.dateOfIssue AS dateOfIssue,
            o.dateOfRevision AS dateOfRevision,
            o.version AS version,
            o.preparedBy AS preparedBy,
            o.sdsNumber AS sdsNumber,
            o.disclaimer AS disclaimer
        FROM OtherInformation o
        WHERE o.sdsId = :sdsId
    """)
    Optional<Section16Projection> findBySdsId(@Param("sdsId") Long sdsId);

    @Query("SELECT o FROM OtherInformation o WHERE o.sdsId = :sdsId")
    Optional<OtherInformation> findEntityBySdsId(@Param("sdsId") Long sdsId);

    @Modifying
    @Query("DELETE FROM OtherInformation o WHERE o.sdsId = :sdsId")
    void deleteBySdsId(@Param("sdsId") Long sdsId);
}