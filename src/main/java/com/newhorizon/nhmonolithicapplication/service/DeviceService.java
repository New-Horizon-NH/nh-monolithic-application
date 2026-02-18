package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.AssignDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.ToggleDeviceEnablementRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnassignDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;

public interface DeviceService {
    <T extends BaseResponse> T createDevice(RegisterDeviceRequestBean requestBean);

    <T extends BaseResponse> T toggleDeviceEnablement(ToggleDeviceEnablementRequestBean requestBean);

    <T extends BaseResponse> T assignDeviceToPatient(AssignDeviceRequestBean requestBean);

    <T extends BaseResponse> T unassignDeviceToPatient(UnassignDeviceRequestBean requestBean);
}
