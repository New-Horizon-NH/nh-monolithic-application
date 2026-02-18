package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.ExamDTO;
import com.newhorizon.nhmonolithicapplication.enums.ExamStatusEnum;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.ExamEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.ExamTypeEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.MedicalChartEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.ExamRepo;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.ExamTypeRepo;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.MedicalChartRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class ExamRepository implements ExamDAO {
    private final ExamTypeRepo examTypeRepo;
    private final MedicalChartRepo medicalChartRepo;
    private final ExamRepo examRepo;

    @Override
    public Optional<ExamDTO> prescribeExam(String examCode, String patientFiscalCode, Instant examDate) {
        ExamTypeEntity examTypeEntity = examTypeRepo.findByExamCode(examCode).orElseThrow();
        MedicalChartEntity medicalChartEntity = medicalChartRepo.findActiveMedicalChart(patientFiscalCode).orElseThrow();
        ExamEntity entity = ExamEntity.builder()
                .medicalChartId(medicalChartEntity.getId())
                .examTypeId(examTypeEntity.getId())
                .examDateTime(examDate)
                .examStatus(ExamStatusEnum.CREATED.getExamStatusCode())
                .build();
        entity = examRepo.saveAndFlush(entity);
        return Optional.of(ExamDTO.builder()
                .examId(entity.getId())
                .build());
    }

    @Override
    public Optional<ExamDTO> findExamById(String examCode) {
        return examRepo.findById(examCode)
                .map(entity -> ExamDTO.builder()
                        .examId(entity.getId())
                        .examStatus(entity.getExamStatus())
                        .build());
    }

    @Override
    public Boolean isExamPrescribedToPatient(String patientFiscalCode, String examId) {
        MedicalChartEntity medicalChartEntity = medicalChartRepo.findActiveMedicalChart(patientFiscalCode).orElseThrow();
        ExamEntity examEntity = examRepo.findById(examId).orElseThrow();
        return examEntity.getMedicalChartId().equals(medicalChartEntity.getId());
    }

    @Override
    public Optional<ExamDTO> unprescribeExam(String examId, String patientFiscalCode) {
        ExamEntity examEntity = examRepo.findById(examId).orElseThrow();
        examEntity.setExamStatus(ExamStatusEnum.UNPRESCRIBED.getExamStatusCode());
        examEntity = examRepo.saveAndFlush(examEntity);
        return Optional.of(ExamDTO.builder()
                .examId(examEntity.getId())
                .examStatus(examEntity.getExamStatus())
                .build());
    }

    @Override
    public Optional<ExamDTO> updateStatus(String examId, Integer examStatusCode) {
        ExamEntity examEntity = examRepo.findById(examId).orElseThrow();
        examEntity.setExamStatus(examStatusCode);
        examEntity = examRepo.saveAndFlush(examEntity);
        return Optional.of(ExamDTO.builder()
                .examId(examEntity.getId())
                .examStatus(examEntity.getExamStatus())
                .build());
    }
}
