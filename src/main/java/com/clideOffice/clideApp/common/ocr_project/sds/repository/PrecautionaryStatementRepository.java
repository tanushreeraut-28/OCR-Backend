package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section2.PrecautionaryStatement;

@Repository
public interface PrecautionaryStatementRepository extends JpaRepository<PrecautionaryStatement, Long> {

    List<PrecautionaryStatement> findByIsMasterTrue();
}