package com.newhorizon.nhmonolithicapplication.beans.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class RetrieveMonthlyShiftsResponseBean extends BaseResponse {
    private List<ShiftResponse> shifts;

    @Data
    @Builder
    public static class ShiftResponse {
        private String shiftId;
        private Integer day;
        private String shiftName;
    }
}
