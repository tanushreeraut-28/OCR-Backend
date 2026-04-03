package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.SdsOcrResult;

import java.util.Optional;

@Repository
public interface SdsOcrResultRepository extends JpaRepository<SdsOcrResult, Long> {

    Optional<SdsOcrResult> findBySdsId(Long sdsId);
}