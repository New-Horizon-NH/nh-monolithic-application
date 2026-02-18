package com.newhorizon.nhmonolithicapplication.mapper;

import com.newhorizon.nhmonolithicapplication.dto.ActiveIngredientDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DrugActiveIngredientEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActiveIngredientMapper {
    DrugActiveIngredientEntity parseDTO(ActiveIngredientDTO dto);

    ActiveIngredientDTO parseEntity(DrugActiveIngredientEntity entity);
}
