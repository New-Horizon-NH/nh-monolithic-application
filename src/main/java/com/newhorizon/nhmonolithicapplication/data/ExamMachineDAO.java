package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.ExamMachineDTO;

import java.util.Optional;

public interface ExamMachineDAO {
    Optional<ExamMachineDTO> createExamMachine(ExamMachineDTO build);

    Optional<ExamMachineDTO> findBySerialNumber(String serialNumber);

    Optional<ExamMachineDTO> findById(String machineId);

    Optional<ExamMachineDTO> toggleMachineEnablement(String machineId, Boolean isEnabled);

}
