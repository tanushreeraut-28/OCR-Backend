package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.clideOffice.clideApp.common.ocr_project.sds.entity.section1.SdsVersion;

@Repository
public interface SdsVersionRepository extends JpaRepository<SdsVersion, Long> {

    // Get latest version by SDS ID
    SdsVersion findTopBySdsMasterIdOrderByVersionNumberDesc(Long sdsId);
}