package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section8.ExposureLimit;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.ExposureLimitProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExposureLimitRepository extends JpaRepository<ExposureLimit, Long> {

    List<ExposureLimitProjection> findBySection8_SdsId(Long sdsId);
}