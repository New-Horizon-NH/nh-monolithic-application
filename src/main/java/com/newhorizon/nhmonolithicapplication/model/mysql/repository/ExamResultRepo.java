package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.ExamResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamResultRepo extends JpaRepository<ExamResultEntity, String> {

    List<ExamResultEntity> findByExamId(String examId);
}
