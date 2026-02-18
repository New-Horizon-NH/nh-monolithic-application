package com.newhorizon.nhmonolithicapplication.mapper;

import com.newhorizon.nhmonolithicapplication.dto.DrugDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DrugEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DrugMapper {
    @Mapping(source = "drugId", target = "id")
    @Mapping(source = "drugName", target = "name")
    @Mapping(source = "drugCode", target = "code")
    DrugEntity parseDTO(DrugDTO dto);

    @Mapping(source = "id", target = "drugId")
    @Mapping(source = "name", target = "drugName")
    @Mapping(source = "code", target = "drugCode")
    DrugDTO parseEntity(DrugEntity entity);
}
