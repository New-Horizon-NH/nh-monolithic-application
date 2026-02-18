package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.ActiveIngredientDTO;

import java.util.Optional;

public interface ActiveIngredientDAO {
    Optional<ActiveIngredientDTO> retrieveByName(String activeIngredient);

    Optional<ActiveIngredientDTO> createActiveIngredient(ActiveIngredientDTO build);

    Optional<ActiveIngredientDTO> retrieveById(String drugId);
}
