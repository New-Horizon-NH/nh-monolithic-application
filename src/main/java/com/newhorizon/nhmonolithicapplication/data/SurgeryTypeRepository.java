package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.SurgeryTypeDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.SurgeryTypeEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.SurgeryTypeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class SurgeryTypeRepository implements SurgeryTypeDAO {
    private final SurgeryTypeRepo surgeryTypeRepo;

    @Override
    public Optional<SurgeryTypeDTO> findBySurgeryCode(String surgeryCode) {
        return surgeryTypeRepo.findBySurgeryCode(surgeryCode)
                .map(entity -> SurgeryTypeDTO.builder()
                        .surgeryTypeId(entity.getId())
                        .surgeryTypeName(entity.getSurgeryName())
                        .surgeryTypeCode(entity.getSurgeryCode())
                        .surgeryTypeDescription(entity.getSurgeryDescription())
                        .build());
    }

    @Override
    public Optional<SurgeryTypeDTO> createSurgeryType(SurgeryTypeDTO build) {
        SurgeryTypeEntity entity = SurgeryTypeEntity.builder()
                .surgeryCode(build.getSurgeryTypeCode())
                .surgeryName(build.getSurgeryTypeName())
                .surgeryDescription(build.getSurgeryTypeDescription())
                .build();
        entity = surgeryTypeRepo.saveAndFlush(entity);
        build.setSurgeryTypeId(entity.getId());
        return Optional.of(build);
    }
}
