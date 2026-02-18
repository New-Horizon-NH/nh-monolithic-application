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
public class SurgeryTypeDTO {
    private String surgeryTypeId;
    private String surgeryTypeName;
    private String surgeryTypeCode;
    private String surgeryTypeDescription;
}
