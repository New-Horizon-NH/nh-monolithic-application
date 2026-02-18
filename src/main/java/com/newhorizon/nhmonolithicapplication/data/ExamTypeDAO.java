package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.ExamTypeDTO;

import java.util.Optional;

public interface ExamTypeDAO {
    Optional<ExamTypeDTO> findByCode(String examCode);

    Optional<ExamTypeDTO> findById(String id);

    Optional<ExamTypeDTO> createExamType(ExamTypeDTO build);

}
