package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section2.HazardPictogram;

@Repository
public interface HazardPictogramRepository extends JpaRepository<HazardPictogram, Long> {
}