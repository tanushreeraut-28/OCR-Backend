package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section1.SdsMaster;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.DashboardSummaryProjection;

@Repository
public interface DashboardSummaryRepository extends JpaRepository<SdsMaster, Long> {

    @Query(value = """
            SELECT 
                COUNT(*) AS totalSds,
                SUM(CASE WHEN status = 'PENDING_REVIEW' THEN 1 ELSE 0 END) AS pendingReview,
                SUM(CASE WHEN status = 'DUPLICATE' THEN 1 ELSE 0 END) AS duplicates
            FROM sds_master
            """, nativeQuery = true)
    DashboardSummaryProjection getDashboardSummary();

}