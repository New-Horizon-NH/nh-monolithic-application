package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.AutoGenerateMonthlyWorkShiftRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveMonthlyShiftsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateShiftRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.GenerateMonthlyWorkShiftResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrieveMonthlyShiftsResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UpdateShiftResponseBean;
import org.springframework.http.ResponseEntity;

public interface ShiftController {
    /**
     * Given the user and the first shift, automatically generate shifts for all the month.
     * this involves the generation of calendar with shifts
     *
     * @param requestBean request
     * @return Entity with managed response of {@link GenerateMonthlyWorkShiftResponseBean}
     */
    ResponseEntity<?> generateMonthlyShift(AutoGenerateMonthlyWorkShiftRequestBean requestBean);

    /**
     * Update single shift with provided new once.
     * this involves the update of calendar
     *
     * @param requestBean request
     * @return Entity with managed response of {@link UpdateShiftResponseBean}
     */
    ResponseEntity<?> updateShift(UpdateShiftRequestBean requestBean);

    /**
     * Retrieve monthly shifts
     *
     * @param requestBean request
     * @return Entity with managed response of {@link RetrieveMonthlyShiftsResponseBean}
     */
    ResponseEntity<?> retrieveMonthlyShifts(RetrieveMonthlyShiftsRequestBean requestBean);
}
