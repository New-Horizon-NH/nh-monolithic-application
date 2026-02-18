package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.PatientDTO;

import java.util.Optional;

public interface PatientDAO {
    Optional<PatientDTO> findPatientByFiscalCode(String fiscalCode);

    Optional<PatientDTO> retrievePatientByExamCode(String examId);

    Optional<PatientDTO> createPatient(PatientDTO build);

}
