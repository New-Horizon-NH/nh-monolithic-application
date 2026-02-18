package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.AssignDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.GenerateDeviceCredentialsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.ToggleDeviceEnablementRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnassignDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.AssignDeviceResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.GenerateDeviceCredentialsResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterDeviceResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.ToggleDeviceEnablementResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UnassignDeviceResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UpdateDeviceResponseBean;
import org.springframework.http.ResponseEntity;

public interface DeviceController {
    /**
     * Generate new monitor credentials. the monitor have to exists in the system otherwise bad request is returned
     *
     * @param requestBean request
     * @return Entity with managed response of {@link GenerateDeviceCredentialsResponseBean}
     */
    ResponseEntity<?> generateDeviceCredentials(GenerateDeviceCredentialsRequestBean requestBean);

    /**
     * Register new monitor with all available devices
     *
     * @param requestBean request
     * @return Entity with managed response of {@link RegisterDeviceResponseBean}
     */
    ResponseEntity<?> registerNewDevice(RegisterDeviceRequestBean requestBean);

    /**
     * Update monitor in any value and configuration
     *
     * @param requestBean request
     * @return Entity with managed response of {@link UpdateDeviceResponseBean}
     */
    ResponseEntity<?> updateDevice(UpdateDeviceRequestBean requestBean);

    /**
     * Assign device to a final user
     *
     * @param requestBean request
     * @return Entity with managed response of {@link AssignDeviceResponseBean}
     */
    ResponseEntity<?> assignDevice(AssignDeviceRequestBean requestBean);

    /**
     * Unassign device to a final user
     *
     * @param requestBean request
     * @return Entity with managed response of {@link UnassignDeviceResponseBean}
     */
    ResponseEntity<?> unassignDevice(UnassignDeviceRequestBean requestBean);

    /**
     * Toggle device enablement
     *
     * @param requestBean request
     * @return Entity with managed response of {@link ToggleDeviceEnablementResponseBean}
     */
    ResponseEntity<?> toggleDevice(ToggleDeviceEnablementRequestBean requestBean);

}
