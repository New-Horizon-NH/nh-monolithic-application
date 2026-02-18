package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.TherapyDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.TherapyEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.TherapyRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class TherapyRepository implements TherapyDAO {
    private final TherapyRepo therapyRepo;

    @Override
    public Optional<TherapyDTO> createSUT(TherapyDTO build) {
        TherapyEntity therapyEntity = TherapyEntity.builder()
                .medicalChartId(build.getMedicalChartId())
                .medicalCreatorId(build.getMedicalCreatorId())
                .build();
        therapyEntity = therapyRepo.saveAndFlush(therapyEntity);
        build.setSutId(therapyEntity.getId());
        return Optional.of(build);
    }

    @Override
    public Optional<TherapyDTO> retrieveById(String sutId) {
        return therapyRepo.findById(sutId)
                .map(entity -> TherapyDTO.builder()
                        .sutId(entity.getId())
                        .medicalChartId(entity.getMedicalChartId())
                        .medicalCreatorId(entity.getMedicalCreatorId())
                        .build());
    }
}
