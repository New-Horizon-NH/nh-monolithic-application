package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.PatientEventAssociationDTO;

import java.util.Optional;

public interface PatientEventAssociationDAO {
    Optional<PatientEventAssociationDTO> createAssociation(PatientEventAssociationDTO build);

}
