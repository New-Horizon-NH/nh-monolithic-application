package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.MedicalDTO;

import java.util.Optional;

public interface MedicalDAO {
    Optional<MedicalDTO> retrieveByFiscalCode(String fiscalCode);

    Optional<MedicalDTO> retrieveById(String medicalId);

}
