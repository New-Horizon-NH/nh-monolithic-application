package com.newhorizon.nhmonolithicapplication.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AddressTypeEnum {
    RESIDENCE(1000, "Residence"),
    DOMICILE(2000, "Domicile");
    private final Integer addressTypeCode;
    private final String addressTypeName;
}
