package com.newhorizon.nhmonolithicapplication.model.mysql.projections;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MonthlyShiftProjectionImpl {
    private LocalDate shiftDate;
    private Integer shiftCode;
}
