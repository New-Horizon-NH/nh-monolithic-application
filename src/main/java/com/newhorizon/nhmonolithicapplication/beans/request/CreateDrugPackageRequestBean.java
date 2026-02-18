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
public class CreateDrugPackageRequestBean {
    private String drugId;
    private String packageId;
    private String name;
    private String aicCode;
    private String fornitureClass;
    private String fornitureClassDescription;
    private String refundabilityClass;
}
