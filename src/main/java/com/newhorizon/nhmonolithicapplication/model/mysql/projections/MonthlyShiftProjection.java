package com.newhorizon.nhmonolithicapplication.model.mysql.projections;

import java.time.LocalDate;

public interface MonthlyShiftProjection {
    LocalDate getShiftDate();

    Integer getShiftCode();
}
