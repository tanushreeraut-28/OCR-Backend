package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.section1.Plant;
import com.clideOffice.clideApp.common.ocr_project.sds.interfaces.GetPlantProjection;

@Repository
public interface GetPlantRepository extends JpaRepository<Plant, Long> {

	@Query(value = """
	        SELECT 
	            p.plant_id AS plantId,
	            p.plant_name AS plantName
	        FROM plants p
	        WHERE p.service_id = :serviceId
	        """, nativeQuery = true)

	List<GetPlantProjection> getPlantsByService(Long serviceId);

}