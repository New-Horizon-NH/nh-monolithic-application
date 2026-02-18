package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.SurgeryDTO;
import com.newhorizon.nhmonolithicapplication.enums.SurgeryStatusEnum;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.SurgeryEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.SurgeryRepo;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.SurgeryTypeRepo;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.SurgicalRoomRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class SurgeryRepository implements SurgeryDAO {
    private final SurgeryRepo surgeryRepo;
    private final SurgeryTypeRepo surgeryTypeRepo;
    private final SurgicalRoomRepo surgicalRoomRepo;

    @Override
    public List<SurgeryDTO> findOverlappingSurgeries(Integer surgicalRoomNumber, Instant startSurgery, Instant endSurgery) {
        return surgeryRepo.findOverlappingSurgeries(surgicalRoomNumber,
                        startSurgery,
                        endSurgery,
                        SurgeryStatusEnum.UNSCHEDULED.getSurgeryTypeCode())
                .stream()
                .map(entity -> SurgeryDTO.builder()
                        .surgeryId(entity.getId())
                        .surgeryStart(entity.getSurgeryStart())
                        .surgeryEnd(entity.getSurgeryEnd())
                        .medicalChartId(entity.getMedicalChartId())
                        .surgeryTypeCode(surgeryTypeRepo.findById(entity.getSurgeryTypeId()).orElseThrow().getSurgeryCode())
                        .surgicalRoomNumber(surgicalRoomRepo.findById(entity.getSurgicalRoomId()).orElseThrow().getRoomNumber())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SurgeryDTO> scheduleSurgery(SurgeryDTO build) {
        String surgeryTypeId = surgeryTypeRepo.findBySurgeryCode(build.getSurgeryTypeCode()).orElseThrow().getId();
        String surgicalRoomId = surgicalRoomRepo.findByRoomNumber(build.getSurgicalRoomNumber()).orElseThrow().getId();
        SurgeryEntity entity = SurgeryEntity.builder()
                .surgicalRoomId(surgicalRoomId)
                .surgeryStart(build.getSurgeryStart())
                .surgeryEnd(build.getSurgeryEnd())
                .medicalChartId(build.getMedicalChartId())
                .surgeryTypeId(surgeryTypeId)
                .surgeryStatus(SurgeryStatusEnum.SCHEDULED.getSurgeryTypeCode())
                .build();
        entity = surgeryRepo.saveAndFlush(entity);
        build.setSurgeryId(entity.getId());
        return Optional.of(build);
    }

    @Override
    public Optional<SurgeryDTO> retrieveScheduledSurgery(String surgeryId) {
        return surgeryRepo.findById(surgeryId)
                .map(entity -> SurgeryDTO.builder()
                        .surgeryId(entity.getId())
                        .surgeryStart(entity.getSurgeryStart())
                        .surgeryEnd(entity.getSurgeryEnd())
                        .medicalChartId(entity.getMedicalChartId())
                        .surgeryTypeCode(surgeryTypeRepo.findById(entity.getSurgeryTypeId()).orElseThrow().getSurgeryCode())
                        .surgicalRoomNumber(surgicalRoomRepo.findById(entity.getSurgicalRoomId()).orElseThrow().getRoomNumber())
                        .build());
    }

    @Override
    public Optional<SurgeryDTO> unscheduleSurgery(String surgeryId) {
        return surgeryRepo.findById(surgeryId)
                .map(entity -> {
                    entity.setSurgeryStatus(SurgeryStatusEnum.UNSCHEDULED.getSurgeryTypeCode());
                    surgeryRepo.saveAndFlush(entity);
                    return SurgeryDTO.builder()
                            .surgeryId(entity.getId())
                            .surgeryStart(entity.getSurgeryStart())
                            .surgeryEnd(entity.getSurgeryEnd())
                            .medicalChartId(entity.getMedicalChartId())
                            .surgeryTypeCode(surgeryTypeRepo.findById(entity.getSurgeryTypeId()).orElseThrow().getSurgeryCode())
                            .surgicalRoomNumber(surgicalRoomRepo.findById(entity.getSurgicalRoomId()).orElseThrow().getRoomNumber())
                            .build();
                });
    }

    @Override
    public Optional<SurgeryDTO> rescheduleSurgery(SurgeryDTO surgeryDTO) {
        return surgeryRepo.findById(surgeryDTO.getSurgeryId())
                .map(entity -> {
                    entity.setSurgeryStart(surgeryDTO.getSurgeryStart());
                    entity.setSurgeryEnd(surgeryDTO.getSurgeryEnd());
                    surgeryRepo.saveAndFlush(entity);
                    return surgeryDTO;
                });
    }
}
