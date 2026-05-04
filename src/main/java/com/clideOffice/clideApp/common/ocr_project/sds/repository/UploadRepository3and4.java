package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section3.Ingredient;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;

import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.UploadProjection3and4;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section1.SdsMaster;

@Repository
public interface UploadRepository3and4 extends JpaRepository<Ingredient, Long> {

    /* ================= DUPLICATE CHECK (INGREDIENT) ================= */
    @Query(value = """
        SELECT COUNT(*) FROM ingredients
        WHERE LOWER(chemical_name) = LOWER(:chemicalName)
        AND cas_number = :casNumber
        """, nativeQuery = true)
    int checkIngredientDuplicate(String chemicalName, String casNumber);


    /* ================= INSERT INGREDIENT ================= */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO ingredients
        (sds_id, chemical_name, cas_number, ec_number,
         concentration_min, concentration_max, classification,
         is_active, created_at, updated_at)
        VALUES
        (:sdsId, :chemicalName, :casNumber, :ecNumber,
         :min, :max, :classification,
         TRUE, NOW(), NOW())
        """, nativeQuery = true)
    void insertIngredient(Long sdsId, String chemicalName, String casNumber,
                          String ecNumber, Double min, Double max, String classification);


    /* ================= INSERT SPECIAL LIMIT ================= */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO special_limits
        (sds_id, substance_name, limit_value, hazard_class,
         remarks, is_active, updated_at)
        VALUES
        (:sdsId, :substanceName, :limitValue, :hazardClass,
         :remarks, TRUE, NOW())
        """, nativeQuery = true)
    void insertSpecialLimit(Long sdsId, String substanceName, String limitValue,
                            String hazardClass, String remarks);


    /* ================= INSERT FIRST AID ================= */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO first_aid_measures
        (sds_id, emergency_overview, general_advice,
         after_inhalation, after_skin_contact,
         after_eye_contact, after_swallowing,
         immediate_medical_attention,
         is_active, updated_at)
        VALUES
        (:sdsId, :overview, :general,
         :inhalation, :skin,
         :eye, :swallow,
         :treatment,
         TRUE, NOW())
        """, nativeQuery = true)
    void insertFirstAid(Long sdsId, String overview, String general,
                        String inhalation, String skin,
                        String eye, String swallow,
                        String treatment);


    /* ================= INSERT SPECIAL TREATMENT ================= */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO first_aid_special_treatment
        (sds_id, treatment, is_active, created_at)
        VALUES
        (:sdsId, :treatment, TRUE, NOW())
        """, nativeQuery = true)
    void insertSpecialTreatment(Long sdsId, String treatment);
}