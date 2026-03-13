package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.SdsMaster;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.OcrExtractedDataProjection;

@Repository
public interface OcrExtractedDataRepository extends JpaRepository<SdsMaster, Long> {

	@Query(value = """
		    SELECT 
		        sm.id AS sdsId,
		        sv.id AS versionId,

		        ss.product_identifier AS productIdentifier,
		        ss.other_identification AS otherIdentification,
		        ss.sds_number AS sdsNumber,
		        ss.recommended_use AS recommendedUse,
		        ss.recommended_restrictions AS recommendedRestrictions,
		        ss.manufacturer_info AS manufacturerInfo,
		        ss.source_type AS sourceType,

		        so.raw_text AS rawText,
		        so.confidence_score AS confidenceScore

		    FROM sds_master sm

		    LEFT JOIN sds_version sv
		        ON sm.id = sv.sds_id
		        AND sv.version_number = sm.current_version

		    LEFT JOIN sds_section1 ss
		        ON sv.id = ss.version_id

		    LEFT JOIN sds_ocr_result so
		        ON sm.id = so.sds_id
		        AND sv.id = so.version_id

		    WHERE sm.id = :sdsId
		""", nativeQuery = true)
		OcrExtractedDataProjection getOcrExtractedData(Long sdsId);

}