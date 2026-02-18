package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.MedicalWorkShiftAssociationDTO;

import java.util.List;
import java.util.Optional;

public interface MedicalWorkShiftAssociationDAO {
    Optional<MedicalWorkShiftAssociationDTO> createAssociation(MedicalWorkShiftAssociationDTO build);

    Optional<MedicalWorkShiftAssociationDTO> findAssociation(String associationId);

    Optional<MedicalWorkShiftAssociationDTO> updateAssociation(MedicalWorkShiftAssociationDTO build);

    List<MedicalWorkShiftAssociationDTO> findMonthlyAssociation(String medicalId, Integer month, Integer year);
}
