package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section2.HazardStatement;

@Repository
public interface HazardStatementRepository extends JpaRepository<HazardStatement, Long> {

    List<HazardStatement> findByIsMasterTrue();
}