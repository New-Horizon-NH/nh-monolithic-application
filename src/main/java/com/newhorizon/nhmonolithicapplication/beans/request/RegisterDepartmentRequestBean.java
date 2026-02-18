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
public class RegisterDepartmentRequestBean {
    private String departmentName;
    private String code;
    private String description;
    private String location;
    private String director;
    private String phone;
    private String email;
    private String coordinator;
}
