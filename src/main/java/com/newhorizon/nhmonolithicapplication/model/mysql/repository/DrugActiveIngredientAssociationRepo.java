package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DrugWithDrugActiveIngredientAssociationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrugActiveIngredientAssociationRepo extends JpaRepository<DrugWithDrugActiveIngredientAssociationEntity, String> {
    Optional<DrugWithDrugActiveIngredientAssociationEntity> findByDrugIdAndActiveIngredientId(String drugId, String activeIngredientId);
}
