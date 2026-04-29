package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section1.SdsOcrResult;

@Repository
public interface SdsOcrResultRepository extends JpaRepository<SdsOcrResult, Long> {

    // Get latest OCR record by SDS ID
    SdsOcrResult findTopBySdsIdOrderByCreatedAtDesc(Long sdsId);
}