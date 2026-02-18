package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.ExamDTO;

import java.time.Instant;
import java.util.Optional;

public interface ExamDAO {
    Optional<ExamDTO> prescribeExam(String examCode, String patientFiscalCode, Instant examDate);

    Optional<ExamDTO> findExamById(String examCode);

    Boolean isExamPrescribedToPatient(String patientFiscalCode, String examId);

    Optional<ExamDTO> unprescribeExam(String examId, String patientFiscalCode);


    Optional<ExamDTO> updateStatus(String examId, Integer examStatusCode);
}
