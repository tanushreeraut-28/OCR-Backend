package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section8.Section8;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Section8Repository extends JpaRepository<Section8, Long> {

    Optional<Section8> findBySdsId(Long sdsId);
}