package com.clideOffice.clideApp.common.ocr_project.sds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.clideOffice.clideApp.common.ocr_project.sds.entity.ClideUser;

import jakarta.transaction.Transactional;

@Repository
public interface RemoveUserRepository extends JpaRepository<ClideUser, Integer> {

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE clide_user
            SET soft_delete = 1
            WHERE user_id = :userId
            """, nativeQuery = true)
    int removeUser(Integer userId);

}