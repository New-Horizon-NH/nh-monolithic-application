package com.newhorizon.nhmonolithicapplication.beans.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class AssignTreatmentToTherapyRequestBean {
    private String sutId;
    private String administrationPackageId;
    private Integer administrationNumber;
    private Integer administrationType;
    private String medicalAssigneeId;
}
