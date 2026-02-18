package com.newhorizon.nhmonolithicapplication.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SurgeryStatusEnum {
    SCHEDULED(1000, "Scheduled"),
    UNSCHEDULED(2000, "Unscheduled");
    private final Integer surgeryTypeCode;
    private final String surgeryTypeName;
}
