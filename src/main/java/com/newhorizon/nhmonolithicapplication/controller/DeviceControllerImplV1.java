package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.AssignDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.GenerateDeviceCredentialsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.ToggleDeviceEnablementRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnassignDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/device")
@RequiredArgsConstructor
@Slf4j
public class DeviceControllerImplV1 extends BaseController implements DeviceController {
    private final DeviceService deviceService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<?> generateDeviceCredentials(@RequestBody GenerateDeviceCredentialsRequestBean requestBean) {
        return handleNotImplemented();
    }

    @Override
    @PostMapping
    public ResponseEntity<?> registerNewDevice(@RequestBody RegisterDeviceRequestBean requestBean) {
        return handleResponse(deviceService.createDevice(requestBean));
    }

    @Override
    @PutMapping
    public ResponseEntity<?> updateDevice(@RequestBody UpdateDeviceRequestBean requestBean) {
        return handleNotImplemented();
    }

    @Override
    @PostMapping("/assign")
    public ResponseEntity<?> assignDevice(@RequestBody AssignDeviceRequestBean requestBean) {
        return handleResponse(deviceService.assignDeviceToPatient(requestBean));
    }

    @Override
    @DeleteMapping("/assign")
    public ResponseEntity<?> unassignDevice(@RequestBody UnassignDeviceRequestBean requestBean) {
        return handleResponse(deviceService.unassignDeviceToPatient(requestBean));
    }

    @Override
    @PutMapping("/toggle")
    public ResponseEntity<?> toggleDevice(@RequestBody ToggleDeviceEnablementRequestBean requestBean) {
        return handleResponse(deviceService.toggleDeviceEnablement(requestBean));
    }
}
