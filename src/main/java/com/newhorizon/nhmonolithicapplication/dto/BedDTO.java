package com.newhorizon.nhmonolithicapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class BedDTO {
    private String departmentCode;
    private Integer roomNumber;
    private Integer bedNumber;
    private Boolean isMotorized;
    private String bedSerialNumber;
}
