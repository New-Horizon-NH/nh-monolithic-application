package com.newhorizon.nhmonolithicapplication.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AdministrationTypeEnum {
    INTRAVENOUS(1000, "Intravenous"),
    INTRAMUSCULAR(1001, "Intramuscular"),
    ORAL_SUSPENSION(2000, "Oral Suspension"),
    OTHER(9000, "Other");

    private final Integer administrationTypeCode;
    private final String administrationTypeName;

    public static AdministrationTypeEnum getByCode(Integer code) {
        Optional<AdministrationTypeEnum> responseCode = Arrays.stream(AdministrationTypeEnum.values())
                .filter(administrationTypeEnum -> code.equals(administrationTypeEnum.getAdministrationTypeCode()))
                .findFirst();
        return responseCode.orElse(null);
    }
}
