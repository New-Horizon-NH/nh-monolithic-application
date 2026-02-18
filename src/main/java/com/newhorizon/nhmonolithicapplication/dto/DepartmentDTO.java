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
public class DepartmentDTO {
    private String departmentName;
    private String code;
    private String description;
    private String location;
    private String director;
    private String phone;
    private String email;
    private String coordinator;
}
