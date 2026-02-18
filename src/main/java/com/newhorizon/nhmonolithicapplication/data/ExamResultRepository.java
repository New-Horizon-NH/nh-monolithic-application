package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.ExamResultDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.ExamResultEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.ExamResultRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class ExamResultRepository implements ExamResultDAO {
    private final ExamResultRepo examResultRepo;

    @Override
    public Optional<ExamResultDTO> saveExamResult(ExamResultDTO build) {
        ExamResultEntity entity = ExamResultEntity.builder()
                .examId(build.getExamId())
                .uploadFilePath(build.getUploadFilePath())
                .publishTimestamp(Instant.now())
                .build();
        examResultRepo.saveAndFlush(entity);
        return Optional.of(build);
    }

    @Override
    public List<ExamResultDTO> retrieveResultByExamId(String examId) {
        return examResultRepo.findByExamId(examId)
                .stream()
                .map(entity -> ExamResultDTO.builder()
                        .examId(examId)
                        .uploadFilePath(entity.getUploadFilePath())
                        .build())
                .collect(Collectors.toList());
    }
}
