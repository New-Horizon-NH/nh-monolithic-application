package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DrugActiveIngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrugActiveIngredientRepo extends JpaRepository<DrugActiveIngredientEntity, String> {
    Optional<DrugActiveIngredientEntity> findByName(String activeIngredient);
}
