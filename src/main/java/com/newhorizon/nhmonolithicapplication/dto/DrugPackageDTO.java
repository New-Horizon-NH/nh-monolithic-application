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
public class DrugPackageDTO {
    private String drugPackageId;
    private String drugId;
    private String packageId;
    private String name;
    private String aicCode;
    private String fornitureClass;
    private String fornitureClassDescription;
    private String refundabilityClass;
    private Long quantity;
}
