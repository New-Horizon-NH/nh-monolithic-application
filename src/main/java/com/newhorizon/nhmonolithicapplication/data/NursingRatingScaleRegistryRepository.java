package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.NursingRatingScaleDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.NursingRatingScaleRegistryEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.NursingRatingScaleRegistryRepo;
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
public class NursingRatingScaleRegistryRepository implements NursingRatingScaleRegistryDAO {
    private final NursingRatingScaleRegistryRepo nursingRatingScaleRegistryRepo;

    @Override
    public List<NursingRatingScaleDTO> retrieveAllByMedicalChart(String medicalChartId) {
        return nursingRatingScaleRegistryRepo.findByMedicalChartId(medicalChartId)
                .stream()
                .map(entity -> NursingRatingScaleDTO.builder()
                        .id(entity.getId())
                        .medicalChartId(entity.getMedicalChartId())
                        .scaleCode(entity.getNursingScaleCode())
                        .value(entity.getNursingScaleValue())
                        .timestamp(entity.getTimestamp())
                        .build())
                .toList();
    }

    @Override
    public Optional<NursingRatingScaleDTO> createRecord(NursingRatingScaleDTO build) {
        NursingRatingScaleRegistryEntity entity = NursingRatingScaleRegistryEntity.builder()
                .medicalChartId(build.getMedicalChartId())
                .nursingScaleCode(build.getScaleCode())
                .nursingScaleValue(build.getValue())
                .timestamp(Instant.now())
                .build();
        nursingRatingScaleRegistryRepo.saveAndFlush(entity);
        return Optional.of(build);
    }
}
