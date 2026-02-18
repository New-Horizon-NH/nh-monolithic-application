package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.TherapyRecordDTO;

import java.util.Optional;

public interface TherapyRecordDAO {
    Boolean isPackageAlreadyAssigned(String sutId,
                                     String administrationPackageId);

    Optional<TherapyRecordDTO> assignTreatment(TherapyRecordDTO build);

    Optional<TherapyRecordDTO> retrieveById(String therapyRecordId);

}
