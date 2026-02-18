package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.ExamMachineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamMachineRepo extends JpaRepository<ExamMachineEntity, String> {

    Optional<ExamMachineEntity> findByMachineSerialNumber(String serialNumber);

}
