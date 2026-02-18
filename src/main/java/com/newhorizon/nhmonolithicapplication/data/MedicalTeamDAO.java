package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.MedicalChartMedicalTeamAssociationDTO;
import com.newhorizon.nhmonolithicapplication.dto.MedicalTeamDTO;

import java.util.Optional;

public interface MedicalTeamDAO {
    Optional<MedicalTeamDTO> retrieveByFiscalCode(String fiscalCode);

    Optional<MedicalTeamDTO> createMember(MedicalTeamDTO build);

    Optional<MedicalChartMedicalTeamAssociationDTO> createPatientAssociation(MedicalChartMedicalTeamAssociationDTO build);

}
