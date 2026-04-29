package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section3.SpecialLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialLimitRepository extends JpaRepository<SpecialLimit, Long> {
    List<SpecialLimit> findBySdsIdAndIsActiveTrue(Long sdsId);
}
