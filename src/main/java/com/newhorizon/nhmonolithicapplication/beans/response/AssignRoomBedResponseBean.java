package com.newhorizon.nhmonolithicapplication.beans.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AssignRoomBedResponseBean extends BaseResponse{
    private Integer bedNumber;
    private Integer roomNumber;
    private String departmentCode;
}
