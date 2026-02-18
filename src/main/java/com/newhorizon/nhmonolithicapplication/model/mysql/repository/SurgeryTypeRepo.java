package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.SurgeryTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurgeryTypeRepo extends JpaRepository<SurgeryTypeEntity, String> {
    Optional<SurgeryTypeEntity> findBySurgeryCode(String surgeryCode);
}
