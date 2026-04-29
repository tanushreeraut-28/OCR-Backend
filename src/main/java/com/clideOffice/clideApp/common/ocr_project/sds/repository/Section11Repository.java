package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.Section11.Section11;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Section11Repository extends JpaRepository<Section11, Long> {

    Optional<Section11> findBySdsId(Long sdsId);
}
