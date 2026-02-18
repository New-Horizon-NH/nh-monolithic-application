package com.newhorizon.nhmonolithicapplication.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AdministrationStatusEnum {
    ADMINISTERED(1000, "Administered"),
    NOT_ADMINISTERED(1001, "Not Administered"),
    REFUSED(2000, "Refused");

    private final Integer administrationTypeCode;
    private final String administrationTypeName;

    public static AdministrationStatusEnum getByCode(Integer code) {
        Optional<AdministrationStatusEnum> responseCode = Arrays.stream(AdministrationStatusEnum.values())
                .filter(administrationTypeEnum -> code.equals(administrationTypeEnum.getAdministrationTypeCode()))
                .findFirst();
        return responseCode.orElse(null);
    }
}
