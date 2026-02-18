package com.newhorizon.nhmonolithicapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class SurgeryDTO {
    private String surgeryId;
    private Instant surgeryStart;
    private Instant surgeryEnd;
    private String medicalChartId;
    private String surgeryTypeCode;
    private Integer surgicalRoomNumber;
}
