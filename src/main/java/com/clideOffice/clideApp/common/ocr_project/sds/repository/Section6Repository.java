package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section6.AccidentalReleaseMeasure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Section6Repository extends JpaRepository<AccidentalReleaseMeasure, Long> {

    // Fetch record by SDS ID
    Optional<AccidentalReleaseMeasure> findBySdsIdAndIsActiveTrue(Long sdsId);

}