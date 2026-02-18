package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.PatientEventAssociationDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.PatientEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.PatientEventAssociationEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.PatientEventAssociationRepo;
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
public class PatientEventAssociationRepository implements PatientEventAssociationDAO {
    private final PatientRepo patientRepo;
    private final PatientEventAssociationRepo patientEventAssociationRepo;

    @Override
    public Optional<PatientEventAssociationDTO> createAssociation(PatientEventAssociationDTO build) {
        PatientEntity patient = patientRepo.findByPatientFiscalCode(build.getPatientFiscalCode()).orElseThrow();
        PatientEventAssociationEntity associationEntity = PatientEventAssociationEntity.builder()
                .patientId(patient.getId())
                .eventId(build.getEventId())
                .build();
        patientEventAssociationRepo.saveAndFlush(associationEntity);
        return Optional.of(build);
    }
}
