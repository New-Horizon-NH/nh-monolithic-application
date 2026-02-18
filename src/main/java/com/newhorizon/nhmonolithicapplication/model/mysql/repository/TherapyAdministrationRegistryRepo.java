package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.TherapyAdministrationRegistryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TherapyAdministrationRegistryRepo extends JpaRepository<TherapyAdministrationRegistryEntity, String> {

    List<TherapyAdministrationRegistryEntity> findAllByTherapyRecordId(String therapyRecordId);

    List<TherapyAdministrationRegistryEntity> findAllByTherapyRecordIdAndAdministrationStatus(String therapyRecordId, Integer administrationStatus);
}
