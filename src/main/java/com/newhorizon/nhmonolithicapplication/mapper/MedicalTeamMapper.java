package com.newhorizon.nhmonolithicapplication.mapper;

import com.newhorizon.nhmonolithicapplication.dto.MedicalTeamDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.MedicalTeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalTeamMapper {
    @Mapping(source = "memberId", target = "id")
    MedicalTeamEntity parseDTO(MedicalTeamDTO dto);

    @Mapping(source = "memberId", target = "id")
    @Mapping(target = "isEnabled", constant = "false")
    MedicalTeamEntity createMedicalEntity(MedicalTeamDTO dto);

    @Mapping(source = "id", target = "memberId")
    MedicalTeamDTO parseEntity(MedicalTeamEntity entity);
}
