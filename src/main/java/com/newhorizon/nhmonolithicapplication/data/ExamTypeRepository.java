package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.ExamTypeDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.ExamTypeEntity;
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
public class ExamTypeRepository implements ExamTypeDAO {
    private final ExamTypeRepo examTypeRepo;

    @Override
    public Optional<ExamTypeDTO> findByCode(String examCode) {
        return examTypeRepo.findByExamCode(examCode)
                .map(entity -> ExamTypeDTO.builder()
                        .examCode(entity.getExamCode())
                        .examName(entity.getExamName())
                        .examDescription(entity.getExamDescription())
                        .build());
    }

    @Override
    public Optional<ExamTypeDTO> findById(String id) {
        return examTypeRepo.findById(id)
                .map(entity -> ExamTypeDTO.builder()
                        .examCode(entity.getExamCode())
                        .examName(entity.getExamName())
                        .examDescription(entity.getExamDescription())
                        .build());
    }

    @Override
    public Optional<ExamTypeDTO> createExamType(ExamTypeDTO build) {
        ExamTypeEntity entity = ExamTypeEntity.builder()
                .examCode(build.getExamCode())
                .examName(build.getExamName())
                .examDescription(build.getExamDescription())
                .build();
        examTypeRepo.saveAndFlush(entity);
        return Optional.of(build);
    }
}
