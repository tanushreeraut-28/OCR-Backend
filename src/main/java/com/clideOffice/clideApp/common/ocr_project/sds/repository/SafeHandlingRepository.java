package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section7.SafeHandling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SafeHandlingRepository extends JpaRepository<SafeHandling, Long> {

    /* ================= FIND BY SDS ID ================= */
    Optional<SafeHandling> findBySdsId(Long sdsId);

}