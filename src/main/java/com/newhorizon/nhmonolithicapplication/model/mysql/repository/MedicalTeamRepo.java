package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.MedicalTeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalTeamRepo extends JpaRepository<MedicalTeamEntity, String> {
    Optional<MedicalTeamEntity> findByFiscalCode(String fiscalCode);
}
