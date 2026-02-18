package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.MedicalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalRepo extends JpaRepository<MedicalEntity, String> {
    Optional<MedicalEntity> findByFiscalCode(String fiscalCode);
}
