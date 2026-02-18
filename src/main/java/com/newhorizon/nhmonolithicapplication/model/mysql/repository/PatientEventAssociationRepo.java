package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.PatientEventAssociationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientEventAssociationRepo extends JpaRepository<PatientEventAssociationEntity, String> {

}
