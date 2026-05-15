package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section15.RegulatoryInformation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Upload15And16Repository
        extends JpaRepository<RegulatoryInformation, Long> {

    // ================= DUPLICATE CHECK =================

    @Query(value = """

            SELECT
            (
                (SELECT COUNT(*) FROM regulatory_information WHERE file_hash = :fileHash)
                +
                (SELECT COUNT(*) FROM other_information WHERE file_hash = :fileHash)
            )

            """, nativeQuery = true)

    Integer checkDuplicateFile(String fileHash);

    // ================= NEXT SDS ID =================

    @Query(value = """

            SELECT COALESCE(MAX(sds_id),0) + 1
            FROM
            (
                SELECT sds_id FROM regulatory_information
                UNION ALL
                SELECT sds_id FROM other_information
            ) AS temp_table

            """, nativeQuery = true)

    Long generateNextSdsId();

    // ================= SECTION 15 =================

    @Modifying
    @Transactional
    @Query(value = """

        INSERT INTO regulatory_information
        (
            sds_id,
            safety_health_environmental_regulations,
            directive2012_18_eu,
            reach_annex_xvii,
            directive2011_65_eu,
            regulation_eu2019_1148,
            regulation_ec273_2004,
            regulation_ec111_2005,
            chemical_safety_assessment,
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
            :safetyHealthEnvironmentalRegulations,
            :directive2012_18_EU,
            :reachAnnexXVII,
            :directive2011_65_EU,
            :regulationEU2019_1148,
            :regulationEC273_2004,
            :regulationEC111_2005,
            :chemicalSafetyAssessment,
            :fileData,
            :fileName,
            :fileType,
            :fileHash,
            TRUE,
            NOW(),
            NOW()
        )

        """, nativeQuery = true)

    void insertSection15Data(

            Long sdsId,

            String safetyHealthEnvironmentalRegulations,

            String directive2012_18_EU,

            String reachAnnexXVII,

            String directive2011_65_EU,

            String regulationEU2019_1148,

            String regulationEC273_2004,

            String regulationEC111_2005,

            String chemicalSafetyAssessment,

            byte[] fileData,

            String fileName,

            String fileType,

            String fileHash
    );

    // ================= SECTION 16 =================

    @Modifying
    @Transactional
    @Query(value = """

        INSERT INTO other_information
        (
            sds_id,
            indication_of_changes,
            abbreviations,
            reference_text,
            classification_procedure,
            h_phrases,
            training_advice,
            additional_information,
            date_of_issue,
            date_of_revision,
            `version`,
            prepared_by,
            sds_number,
            disclaimer,
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
            :indicationOfChanges,
            :abbreviations,
            :references,
            :classificationProcedure,
            :hPhrases,
            :trainingAdvice,
            :additionalInformation,
            :dateOfIssue,
            :dateOfRevision,
            :versionValue,
            :preparedBy,
            :sdsNumber,
            :disclaimer,
            :fileData,
            :fileName,
            :fileType,
            :fileHash,
            TRUE,
            NOW(),
            NOW()
        )

        """, nativeQuery = true)

    void insertSection16Data(

            Long sdsId,

            String indicationOfChanges,

            String abbreviations,

            String references,

            String classificationProcedure,

            String hPhrases,

            String trainingAdvice,

            String additionalInformation,

            String dateOfIssue,

            String dateOfRevision,

            String versionValue,

            String preparedBy,

            String sdsNumber,

            String disclaimer,

            byte[] fileData,

            String fileName,

            String fileType,

            String fileHash
    );
}