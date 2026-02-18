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
public class TherapyRecordDTO {
    private String treatmentId;
    private String therapyId;
    private String packageId;
    private Integer administrationNumber;//when 0 it means when needed
    private Integer administrationType;
    private String medicalAssigneeId;
}
