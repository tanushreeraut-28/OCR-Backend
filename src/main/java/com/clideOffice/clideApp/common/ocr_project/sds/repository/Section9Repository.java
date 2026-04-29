package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section9.PhysicalChemicalProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Section9Repository extends JpaRepository<PhysicalChemicalProperties, Long> {

    Optional<PhysicalChemicalProperties> findBySdsId(Long sdsId);
}