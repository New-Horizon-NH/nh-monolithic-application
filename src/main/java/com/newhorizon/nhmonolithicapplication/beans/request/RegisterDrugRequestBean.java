package com.newhorizon.nhmonolithicapplication.beans.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class RegisterDrugRequestBean {
    private String drugName;
    private String drugCode;
    private String pharmaceuticalCompany;
    private String pharmaceuticalForm;
    private String dosageFormDescription;
    private List<String> activeIngredients;
}
