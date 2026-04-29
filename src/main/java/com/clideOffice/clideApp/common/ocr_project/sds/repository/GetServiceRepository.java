package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section1.Services;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.GetServiceProjection;

@Repository
public interface GetServiceRepository extends JpaRepository<Services, Long> {

    @Query(value = """
        SELECT 
            s.service_id AS serviceId,
            s.service_name AS serviceName
        FROM services s
        ORDER BY s.service_name
        """, nativeQuery = true)
    List<GetServiceProjection> getAllServices();

}