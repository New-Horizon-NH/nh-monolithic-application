package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DrugPackageWithdrawalRegistryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugPackageWithdrawalRegistryRepo extends JpaRepository<DrugPackageWithdrawalRegistryEntity, String> {
}
