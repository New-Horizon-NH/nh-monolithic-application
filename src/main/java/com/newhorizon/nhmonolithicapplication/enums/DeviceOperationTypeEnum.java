package com.newhorizon.nhmonolithicapplication.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum DeviceOperationTypeEnum {
    ASSIGNED(1000, "Assigned"),
    RETURNED(2000, "Returned");
    private final Integer deviceOperationTypeCode;
    private final String deviceOperationTypeName;

    public static DeviceOperationTypeEnum getOperationByCode(Integer code) {
        Optional<DeviceOperationTypeEnum> operation = Arrays.stream(DeviceOperationTypeEnum.values())
                .filter(deviceOperationTypeEnum -> code.equals(deviceOperationTypeEnum.getDeviceOperationTypeCode()))
                .findFirst();
        return operation.orElse(null);
    }
}
