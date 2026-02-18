package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.TherapyRecordDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.TherapyRecordEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.TherapyRecordRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class TherapyRecordRepository implements TherapyRecordDAO {
    private final TherapyRecordRepo therapyRecordRepo;

    @Override
    public Boolean isPackageAlreadyAssigned(String sutId, String administrationPackageId) {
        return therapyRecordRepo.existsByTherapyIdAndPackageId(sutId, administrationPackageId);
    }

    @Override
    public Optional<TherapyRecordDTO> assignTreatment(TherapyRecordDTO build) {
        TherapyRecordEntity entity = TherapyRecordEntity.builder()
                .therapyId(build.getTherapyId())
                .packageId(build.getPackageId())
                .administrationNumber(build.getAdministrationNumber())
                .administrationType(build.getAdministrationType())
                .medicalAssigneeId(build.getMedicalAssigneeId())
                .build();
        entity = therapyRecordRepo.saveAndFlush(entity);
        build.setTreatmentId(entity.getId());
        return Optional.of(build);
    }

    @Override
    public Optional<TherapyRecordDTO> retrieveById(String therapyRecordId) {
        return therapyRecordRepo.findById(therapyRecordId)
                .map(entity -> TherapyRecordDTO.builder()
                        .treatmentId(entity.getId())
                        .therapyId(entity.getTherapyId())
                        .packageId(entity.getPackageId())
                        .administrationNumber(entity.getAdministrationNumber())
                        .administrationType(entity.getAdministrationType())
                        .medicalAssigneeId(entity.getMedicalAssigneeId())
                        .build());
    }
}
