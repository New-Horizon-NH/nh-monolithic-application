package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.ExamMachineDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.ExamMachineEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.ExamTypeEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.ExamMachineRepo;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.ExamTypeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class ExamMachineRepository implements ExamMachineDAO {
    private final ExamMachineRepo examMachineRepo;
    private final ExamTypeRepo examTypeRepo;

    @Override
    public Optional<ExamMachineDTO> createExamMachine(ExamMachineDTO build) {
        ExamTypeEntity examTypeEntity = examTypeRepo.findByExamCode(build.getExamCode()).orElseThrow();
        ExamMachineEntity entity = ExamMachineEntity.builder()
                .examTypeId(examTypeEntity.getId())
                .machineName(build.getMachineName())
                .machineSerialNumber(build.getMachineSerialNumber())
                .isEnabled(Boolean.FALSE)
                .build();
        String id = examMachineRepo.saveAndFlush(entity).getId();
        build.setMachineId(id);
        return Optional.of(build);
    }

    @Override
    public Optional<ExamMachineDTO> findBySerialNumber(String serialNumber) {
        return examMachineRepo.findByMachineSerialNumber(serialNumber)
                .map(entity -> ExamMachineDTO.builder()
                        .machineId(entity.getId())
                        .machineSerialNumber(entity.getMachineSerialNumber())
                        .machineName(entity.getMachineName())
                        .build());
    }

    @Override
    public Optional<ExamMachineDTO> findById(String machineId) {
        return examMachineRepo.findById(machineId)
                .map(entity -> ExamMachineDTO.builder()
                        .machineId(entity.getId())
                        .machineSerialNumber(entity.getMachineSerialNumber())
                        .machineName(entity.getMachineName())
                        .build());
    }

    @Override
    public Optional<ExamMachineDTO> toggleMachineEnablement(String machineId, Boolean isEnabled) {
        ExamMachineEntity entity = examMachineRepo.findById(machineId).orElseThrow();
        entity.setIsEnabled(isEnabled);
        entity = examMachineRepo.saveAndFlush(entity);
        return Optional.of(ExamMachineDTO.builder()
                .machineId(entity.getId())
                .machineSerialNumber(entity.getMachineSerialNumber())
                .machineName(entity.getMachineName())
                .build());
    }
}
