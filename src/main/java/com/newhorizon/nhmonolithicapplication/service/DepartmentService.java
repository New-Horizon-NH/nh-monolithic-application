package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.AssignRoomBedRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterBedRoomRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterDepartmentRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterRoomRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnassignRoomBedRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;

public interface DepartmentService {
    <T extends BaseResponse> T registerDepartement(RegisterDepartmentRequestBean requestBean);

    <T extends BaseResponse> T registerRoom(RegisterRoomRequestBean requestBean);

    <T extends BaseResponse> T registerBedRoom(RegisterBedRoomRequestBean requestBean);

    <T extends BaseResponse> T assignBedToPatient(AssignRoomBedRequestBean requestBean);

    <T extends BaseResponse> T unassignBedToPatient(UnassignRoomBedRequestBean requestBean);
}
