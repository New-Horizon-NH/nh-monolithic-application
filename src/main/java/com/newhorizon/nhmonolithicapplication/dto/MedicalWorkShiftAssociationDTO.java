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
public class MedicalWorkShiftAssociationDTO {
    private String associationId;
    private String medicalId;
    private String workShiftId;
    private LocalDate shiftDate;
}
