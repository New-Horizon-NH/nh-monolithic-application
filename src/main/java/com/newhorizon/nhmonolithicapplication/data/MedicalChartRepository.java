package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.MedicalChartDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.MedicalChartEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.PatientEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.MedicalChartRepo;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.PatientRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class MedicalChartRepository implements MedicalChartDAO {
    private final MedicalChartRepo medicalChartRepo;
    private final PatientRepo patientRepo;

    @Override
    public Optional<MedicalChartDTO> retrieveOpenMedicalChart(String patientFiscalCode) {
        return medicalChartRepo.findActiveMedicalChart(patientFiscalCode.toUpperCase())
                .map(entity -> MedicalChartDTO.builder()
                        .medicalChartId(entity.getId())
                        .openingDate(entity.getOpeningDate())
                        .closingDate(entity.getClosingDate())
                        .build());
    }

    @Override
    public Optional<MedicalChartDTO> openMedicalChart(String patientFiscalCode) {
        PatientEntity patientEntity = patientRepo.findByPatientFiscalCode(patientFiscalCode).orElseThrow();
        MedicalChartEntity entity = MedicalChartEntity.builder()
                .patientId(patientEntity.getId())
                .openingDate(Instant.now())
                .build();
        entity = medicalChartRepo.saveAndFlush(entity);
        return Optional.of(MedicalChartDTO.builder()
                .medicalChartId(entity.getId())
                .openingDate(entity.getOpeningDate())
                .closingDate(entity.getClosingDate())
                .build());
    }

    @Override
    public Optional<MedicalChartDTO> retrieveMedicalChart(String medicalChartId) {
        return medicalChartRepo.findById(medicalChartId)
                .map(entity -> MedicalChartDTO.builder()
                        .medicalChartId(entity.getId())
                        .openingDate(entity.getOpeningDate())
                        .closingDate(entity.getClosingDate())
                        .build());
    }

    @Override
    public List<MedicalChartDTO> retrieveFollowedByHospitalMemberFiscalCode(String loggedMemberFiscalCode) {
        return medicalChartRepo.findFollowedByMedicalFiscalCode(loggedMemberFiscalCode)
                .stream()
                .map(entity -> MedicalChartDTO.builder()
                        .medicalChartId(entity.getId())
                        .openingDate(entity.getOpeningDate())
                        .closingDate(entity.getClosingDate())
                        .build())
                .toList();
    }
}
