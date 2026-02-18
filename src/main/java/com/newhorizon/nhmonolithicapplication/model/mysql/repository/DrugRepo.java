package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DrugEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrugRepo extends JpaRepository<DrugEntity, String> {
    Optional<DrugEntity> findByNameAndPharmaceuticalCompanyAndPharmaceuticalFormAndDosageFormDescription(String drugName,
                                                                                                         String pharmaceuticalCompany,
                                                                                                         String pharmaceuticalForm,
                                                                                                         String dosageFormDescription);
}
