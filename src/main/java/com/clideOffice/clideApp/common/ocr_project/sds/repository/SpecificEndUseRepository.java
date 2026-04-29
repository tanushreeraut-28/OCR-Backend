package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section7.SpecificEndUse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecificEndUseRepository extends JpaRepository<SpecificEndUse, Long> {

    Optional<SpecificEndUse> findBySdsId(Long sdsId);
}