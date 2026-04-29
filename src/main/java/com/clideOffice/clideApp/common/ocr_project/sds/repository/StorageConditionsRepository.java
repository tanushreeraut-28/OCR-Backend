package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section7.SafeStorage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageConditionsRepository extends JpaRepository<SafeStorage, Long> {

    Optional<SafeStorage> findBySdsId(Long sdsId);
}