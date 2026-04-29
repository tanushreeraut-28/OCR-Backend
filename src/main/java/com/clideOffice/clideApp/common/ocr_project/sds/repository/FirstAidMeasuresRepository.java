package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section4.FirstAidMeasures;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FirstAidMeasuresRepository extends JpaRepository<FirstAidMeasures, Long> {

    List<FirstAidMeasures> findBySdsIdAndIsActiveTrue(Long sdsId);
}