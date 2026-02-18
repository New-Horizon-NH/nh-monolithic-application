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
public class TherapyAdministrationRegistryDTO {
    private String id;
    private String therapyRecordId;
    private Instant administrationInstant;
    private String administratorId;
    private Integer administrationStatus;
    private String extraInfo;
}
