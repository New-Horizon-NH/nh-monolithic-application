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
public class MedicalDTO {
    private String memberId;
    private String fiscalCode;
    private String name;
    private String surname;
    private String commaSeparatedRoles;
}
