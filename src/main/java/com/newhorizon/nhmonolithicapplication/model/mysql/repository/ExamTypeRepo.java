package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.ExamTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamTypeRepo extends JpaRepository<ExamTypeEntity, String> {
    Optional<ExamTypeEntity> findByExamCode(String examCode);

}
