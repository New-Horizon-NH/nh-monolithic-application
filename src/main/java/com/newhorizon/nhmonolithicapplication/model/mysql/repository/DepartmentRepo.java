package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepo extends JpaRepository<DepartmentEntity, String> {
    Optional<DepartmentEntity> findByCode(String code);
}
