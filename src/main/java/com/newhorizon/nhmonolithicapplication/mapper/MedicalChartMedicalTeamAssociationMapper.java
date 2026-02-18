package com.newhorizon.nhmonolithicapplication.mapper;

import com.newhorizon.nhmonolithicapplication.dto.MedicalChartMedicalTeamAssociationDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.MedicalChartMedicalTeamAssociationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalChartMedicalTeamAssociationMapper {
    MedicalChartMedicalTeamAssociationEntity parseDTO(MedicalChartMedicalTeamAssociationDTO dto);

    MedicalChartMedicalTeamAssociationDTO parseEntity(MedicalChartMedicalTeamAssociationEntity entity);
}
