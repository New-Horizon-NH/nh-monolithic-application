package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.MedicalWorkShiftAssociationDTO;
import com.newhorizon.nhmonolithicapplication.mapper.MedicalWorkShiftAssociationMapper;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.MedicalWorkShiftAssociationEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.MedicalWorkShiftAssociationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class MedicalWorkShiftAssociationRepository implements MedicalWorkShiftAssociationDAO {
    private final MedicalWorkShiftAssociationRepo medicalWorkShiftAssociationRepo;
    private final MedicalWorkShiftAssociationMapper medicalWorkShiftAssociationMapper;

    @Override
    public Optional<MedicalWorkShiftAssociationDTO> createAssociation(MedicalWorkShiftAssociationDTO build) {
        return Optional.of(medicalWorkShiftAssociationMapper.parseEntity(
                medicalWorkShiftAssociationRepo.saveAndFlush(
                        medicalWorkShiftAssociationMapper.parseDTO(build)
                )
        ));
    }

    @Override
    public Optional<MedicalWorkShiftAssociationDTO> findAssociation(String associationId) {
        return medicalWorkShiftAssociationRepo.findById(associationId)
                .map(medicalWorkShiftAssociationMapper::parseEntity);
    }

    @Override
    public Optional<MedicalWorkShiftAssociationDTO> updateAssociation(MedicalWorkShiftAssociationDTO build) {
        MedicalWorkShiftAssociationEntity entity = medicalWorkShiftAssociationRepo.findById(build.getAssociationId()).orElseThrow();
        entity.setWorkShiftId(build.getWorkShiftId());
        entity = medicalWorkShiftAssociationRepo.saveAndFlush(entity);
        return Optional.of(medicalWorkShiftAssociationMapper.parseEntity(entity));
    }

    @Override
    public List<MedicalWorkShiftAssociationDTO> findMonthlyAssociation(String medicalId,
                                                                       Integer month,
                                                                       Integer year) {
        return medicalWorkShiftAssociationRepo.findMonthShifts(medicalId,
                        LocalDate.of(year, month, 1),
                        LocalDate.of(year, month, YearMonth.of(year, month).lengthOfMonth()))
                .stream()
                .map(medicalWorkShiftAssociationMapper::parseEntity)
                .toList();
    }
}
