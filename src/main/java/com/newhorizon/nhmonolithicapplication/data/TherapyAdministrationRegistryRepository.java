package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.TherapyAdministrationRegistryDTO;
import com.newhorizon.nhmonolithicapplication.enums.AdministrationStatusEnum;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.TherapyAdministrationRegistryEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.TherapyAdministrationRegistryRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class TherapyAdministrationRegistryRepository implements TherapyAdministrationRegistryDAO {
    private final TherapyAdministrationRegistryRepo therapyAdministrationRegistryRepo;

    @Override
    public Optional<TherapyAdministrationRegistryDTO> register(TherapyAdministrationRegistryDTO build) {
        TherapyAdministrationRegistryEntity entity = TherapyAdministrationRegistryEntity.builder()
                .therapyRecordId(build.getTherapyRecordId())
                .administrationInstant(build.getAdministrationInstant())
                .administratorId(build.getAdministratorId())
                .administrationStatus(build.getAdministrationStatus())
                .extraInfo(build.getExtraInfo())
                .build();
        therapyAdministrationRegistryRepo.saveAndFlush(entity);
        return Optional.of(build);
    }

    @Override
    public List<TherapyAdministrationRegistryDTO> retrieveByTherapyRecord(String therapyRecordId) {
        return therapyAdministrationRegistryRepo.findAllByTherapyRecordId(therapyRecordId)
                .stream()
                .map(entity -> TherapyAdministrationRegistryDTO.builder()
                        .id(entity.getId())
                        .therapyRecordId(entity.getTherapyRecordId())
                        .administrationInstant(entity.getAdministrationInstant())
                        .administratorId(entity.getAdministratorId())
                        .administrationStatus(entity.getAdministrationStatus())
                        .extraInfo(entity.getExtraInfo())
                        .build())
                .toList();
    }

    @Override
    public List<TherapyAdministrationRegistryDTO> retrieveByTherapyRecordWithState(String therapyRecordId,
                                                                                   AdministrationStatusEnum statusEnum) {
        return therapyAdministrationRegistryRepo.findAllByTherapyRecordIdAndAdministrationStatus(therapyRecordId,
                statusEnum.getAdministrationTypeCode())
                .stream()
                .map(entity -> TherapyAdministrationRegistryDTO.builder()
                        .id(entity.getId())
                        .therapyRecordId(entity.getTherapyRecordId())
                        .administrationInstant(entity.getAdministrationInstant())
                        .administratorId(entity.getAdministratorId())
                        .administrationStatus(entity.getAdministrationStatus())
                        .extraInfo(entity.getExtraInfo())
                        .build())
                .toList();
    }
}
