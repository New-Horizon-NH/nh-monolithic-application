package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.CreateSurgeryRoomRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateSurgeryTypeRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RescheduleSurgeryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.ScheduleSurgeryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnscheduleSurgeryRequestBean;
import com.newhorizon.nhmonolithicapplication.service.SurgeryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/surgery")
@RequiredArgsConstructor
@Slf4j
public class SurgeryControllerImplV1 extends BaseController implements SurgeryController {
    private final SurgeryService surgeryService;

    @Override
    @PostMapping("/room")
    public ResponseEntity<?> createSurgeryRoom(CreateSurgeryRoomRequestBean requestBean) {
        return handleResponse(surgeryService.createSurgeryRoom(requestBean));
    }

    @Override
    @PostMapping("/room/type")
    public ResponseEntity<?> createSurgeryType(CreateSurgeryTypeRequestBean requestBean) {
        return handleResponse(surgeryService.createSurgeryType(requestBean));
    }

    @Override
    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleSurgery(ScheduleSurgeryRequestBean requestBean) {
        return handleResponse(surgeryService.scheduleSurgery(requestBean));
    }

    @Override
    @DeleteMapping("/schedule")
    public ResponseEntity<?> unscheduleSurgery(UnscheduleSurgeryRequestBean requestBean) {
        return handleResponse(surgeryService.unscheduleSurgery(requestBean));
    }

    @Override
    @PutMapping("/schedule")
    public ResponseEntity<?> rescheduleSurgery(RescheduleSurgeryRequestBean requestBean) {
        return handleResponse(surgeryService.rescheduleSurgery(requestBean));
    }
}
