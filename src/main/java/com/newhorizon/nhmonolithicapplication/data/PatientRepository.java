package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.PatientDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.PatientEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.PatientRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class PatientRepository implements PatientDAO {
    private final PatientRepo patientRepo;

    @Override
    public Optional<PatientDTO> findPatientByFiscalCode(String fiscalCode) {
        return patientRepo.findByPatientFiscalCode(fiscalCode)
                .map(entity -> PatientDTO.builder()
                        .patientName(entity.getPatientName())
                        .patientSurname(entity.getPatientSurname())
                        .dateOfBirth(entity.getDateOfBirth())
                        .patientFiscalCode(entity.getPatientFiscalCode())
                        .patientGender(entity.getPatientGender())
                        .build());
    }

    @Override
    public Optional<PatientDTO> retrievePatientByExamCode(String examId) {
        return patientRepo.findByExamCode(examId)
                .map(entity -> PatientDTO.builder()
                        .patientName(entity.getPatientName())
                        .patientSurname(entity.getPatientSurname())
                        .dateOfBirth(entity.getDateOfBirth())
                        .patientFiscalCode(entity.getPatientFiscalCode())
                        .patientGender(entity.getPatientGender())
                        .build());
    }

    @Override
    public Optional<PatientDTO> createPatient(PatientDTO build) {
        PatientEntity entity = PatientEntity.builder()
                .patientName(build.getPatientName())
                .patientSurname(build.getPatientSurname())
                .dateOfBirth(build.getDateOfBirth())
                .patientFiscalCode(build.getPatientFiscalCode())
                .patientGender(build.getPatientGender())
                .build();
        patientRepo.saveAndFlush(entity);
        return Optional.of(build);
    }
}
