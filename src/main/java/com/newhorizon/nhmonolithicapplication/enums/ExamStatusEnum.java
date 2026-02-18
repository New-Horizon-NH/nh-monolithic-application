package com.newhorizon.nhmonolithicapplication.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ExamStatusEnum {
    CREATED(1000, "Created"),
    SAMPLE_TAKEN(1001, "Sample Taken"),
    SAMPLE_PROCESSING(1002, "Sample Processing"),
    RESULTS_PUBLISHED(1003, "Results Published"),
    UNPRESCRIBED(2000, "Unprescribed");

    private final Integer examStatusCode;
    private final String examStatusName;

    public static ExamStatusEnum getStatusByCode(Integer code) {
        Optional<ExamStatusEnum> status = Arrays.stream(ExamStatusEnum.values())
                .filter(examStatusEnum -> code.equals(examStatusEnum.getExamStatusCode()))
                .findFirst();
        return status.orElse(null);
    }
}
