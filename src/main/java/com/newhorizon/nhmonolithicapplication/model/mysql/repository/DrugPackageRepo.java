package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DrugPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrugPackageRepo extends JpaRepository<DrugPackageEntity, String> {
    Optional<DrugPackageEntity> findByPackageId(String packageId);
}
