package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.CreateSurgeryRoomRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateSurgeryTypeRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RescheduleSurgeryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.ScheduleSurgeryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnscheduleSurgeryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;

public interface SurgeryService {
    <T extends BaseResponse> T createSurgeryRoom(CreateSurgeryRoomRequestBean requestBean);

    <T extends BaseResponse> T createSurgeryType(CreateSurgeryTypeRequestBean requestBean);

    <T extends BaseResponse> T scheduleSurgery(ScheduleSurgeryRequestBean requestBean);

    <T extends BaseResponse> T unscheduleSurgery(UnscheduleSurgeryRequestBean requestBean);

    <T extends BaseResponse> T rescheduleSurgery(RescheduleSurgeryRequestBean requestBean);
}
