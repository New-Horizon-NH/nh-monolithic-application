package com.newhorizon.nhmonolithicapplication.beans.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class RetrievePatientAnagraficaResponseBean extends BaseResponse{
    private String patientName;
    private String patientSurname;
    private LocalDate dateOfBirth;
    private String patientFiscalCode;
    private Integer patientGender;
}
