package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.ExamResultDTO;

import java.util.List;
import java.util.Optional;

public interface ExamResultDAO {
    Optional<ExamResultDTO> saveExamResult(ExamResultDTO build);

    List<ExamResultDTO> retrieveResultByExamId(String examId);
}
