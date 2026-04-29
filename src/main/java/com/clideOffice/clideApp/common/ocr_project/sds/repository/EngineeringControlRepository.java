package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section8.EngineeringControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EngineeringControlRepository extends JpaRepository<EngineeringControl, Long> {

    Optional<EngineeringControl> findBySection8_SdsId(Long sdsId);

}