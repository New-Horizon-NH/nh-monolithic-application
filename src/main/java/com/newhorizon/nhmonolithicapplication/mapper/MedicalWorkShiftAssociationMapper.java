package com.newhorizon.nhmonolithicapplication.mapper;

import com.newhorizon.nhmonolithicapplication.dto.MedicalWorkShiftAssociationDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.MedicalWorkShiftAssociationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalWorkShiftAssociationMapper {
    @Mapping(source = "associationId", target = "id")
    MedicalWorkShiftAssociationEntity parseDTO(MedicalWorkShiftAssociationDTO dto);

    @Mapping(source = "id", target = "associationId")
    MedicalWorkShiftAssociationDTO parseEntity(MedicalWorkShiftAssociationEntity entity);
}
