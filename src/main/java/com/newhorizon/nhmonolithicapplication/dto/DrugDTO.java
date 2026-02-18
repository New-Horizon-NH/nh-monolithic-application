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
public class DrugDTO {
    private String drugId;
    private String drugName;
    private String drugCode;
    private String pharmaceuticalCompany;
    private String pharmaceuticalForm;
    private String dosageFormDescription;
}
