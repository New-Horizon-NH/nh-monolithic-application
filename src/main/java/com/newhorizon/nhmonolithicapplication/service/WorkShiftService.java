package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.AutoGenerateMonthlyWorkShiftRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveMonthlyShiftsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateShiftRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;

public interface WorkShiftService {

    <T extends BaseResponse> T autogenerateWorkShift(AutoGenerateMonthlyWorkShiftRequestBean requestBean);

    <T extends BaseResponse> T retrieveMonthlyShifts(RetrieveMonthlyShiftsRequestBean requestBean);

    <T extends BaseResponse> T updateShift(UpdateShiftRequestBean requestBean);

}
