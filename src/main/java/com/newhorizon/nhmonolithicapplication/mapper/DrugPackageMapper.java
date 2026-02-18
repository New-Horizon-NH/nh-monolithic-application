package com.newhorizon.nhmonolithicapplication.mapper;

import com.newhorizon.nhmonolithicapplication.dto.DrugPackageDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DrugPackageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DrugPackageMapper {
    @Mapping(source = "id", target = "drugPackageId")
    DrugPackageDTO parseEntity(DrugPackageEntity entity);
}
