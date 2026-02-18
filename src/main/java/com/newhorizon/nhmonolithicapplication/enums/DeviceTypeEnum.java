package com.newhorizon.nhmonolithicapplication.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum DeviceTypeEnum {
    MULTI_PARAMETER_MONITOR(1000, "Multi-parameter Monitor");
    private final Integer deviceTypeCode;
    private final String deviceTypeName;
}
