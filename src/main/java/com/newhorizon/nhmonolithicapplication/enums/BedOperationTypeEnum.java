package com.newhorizon.nhmonolithicapplication.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum BedOperationTypeEnum {
    ASSIGNED(1000, "Assigned"),
    RETURNED(2000, "Returned");
    private final Integer deviceOperationTypeCode;
    private final String deviceOperationTypeName;

    public static BedOperationTypeEnum getOperationByCode(Integer code) {
        Optional<BedOperationTypeEnum> operation = Arrays.stream(BedOperationTypeEnum.values())
                .filter(bedOperationTypeEnum -> code.equals(bedOperationTypeEnum.getDeviceOperationTypeCode()))
                .findFirst();
        return operation.orElse(null);
    }
}
