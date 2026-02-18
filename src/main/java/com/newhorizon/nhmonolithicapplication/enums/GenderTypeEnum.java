package com.newhorizon.nhmonolithicapplication.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum GenderTypeEnum {
    MALE(1000, "Male"),
    FEMALE(2000, "Female");

    private final Integer genderCode;
    private final String genderName;

    public static GenderTypeEnum getGenderByCode(Integer code) {
        Optional<GenderTypeEnum> status = Arrays.stream(GenderTypeEnum.values())
                .filter(genderTypeEnum -> code.equals(genderTypeEnum.getGenderCode()))
                .findFirst();
        return status.orElse(null);
    }
}
