package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.DrugActiveIngredientAssociationDTO;

import java.util.Optional;

public interface DrugActiveIngredientAssociationDAO {
    Optional<DrugActiveIngredientAssociationDTO> retrieveAssociation(String drugId, String activeIngredientId);

    Optional<DrugActiveIngredientAssociationDTO> createAssociation(DrugActiveIngredientAssociationDTO build);

}
