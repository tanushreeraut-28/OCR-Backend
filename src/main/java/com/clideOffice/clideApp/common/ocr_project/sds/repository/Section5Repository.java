package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section5.Section5Firefighting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Section5Repository extends JpaRepository<Section5Firefighting, Long> {

    Optional<Section5Firefighting> findBySdsId(Long sdsId);
}