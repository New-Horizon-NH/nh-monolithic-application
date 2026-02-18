package com.newhorizon.nhmonolithicapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class PatientDTO {
    private String patientName;
    private String patientSurname;
    private LocalDate dateOfBirth;
    private String patientFiscalCode;
    private Integer patientGender;
}
