package com.newhorizon.nhmonolithicapplication.mapper;

import com.newhorizon.nhmonolithicapplication.dto.DrugActiveIngredientAssociationDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DrugWithDrugActiveIngredientAssociationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DrugActiveIngredientAssociationMapper {
    DrugWithDrugActiveIngredientAssociationEntity parseDTO(DrugActiveIngredientAssociationDTO dto);

    DrugActiveIngredientAssociationDTO parseEntity(DrugWithDrugActiveIngredientAssociationEntity entity);
}
