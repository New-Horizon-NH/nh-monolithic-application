package com.newhorizon.nhmonolithicapplication.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum NursingRatingScaleEnum {
    BRISTOL(1000, "Bristol", 1, 7, -1);
    private final Integer nursingScaleCode;
    private final String nursingScaleName;
    private final Integer min;
    private final Integer max;
    private final Integer cutoff;

    public static NursingRatingScaleEnum getScaleByCode(Integer code) {
        Optional<NursingRatingScaleEnum> operation = Arrays.stream(NursingRatingScaleEnum.values())
                .filter(nursingRatingScaleEnum -> code.equals(nursingRatingScaleEnum.getNursingScaleCode()))
                .findFirst();
        return operation.orElse(null);
    }
}
