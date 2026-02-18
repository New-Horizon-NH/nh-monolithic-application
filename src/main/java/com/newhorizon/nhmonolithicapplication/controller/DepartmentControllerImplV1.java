package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.AssignRoomBedRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterBedRoomRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterDepartmentRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterRoomRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnassignRoomBedRequestBean;
import com.newhorizon.nhmonolithicapplication.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/department")
@RequiredArgsConstructor
@Slf4j
public class DepartmentControllerImplV1 extends BaseController implements DepartmentController {
    private final DepartmentService departmentService;

    @Override
    @PostMapping
    public ResponseEntity<?> registerDepartment(@RequestBody RegisterDepartmentRequestBean requestBean) {
        return handleResponse(departmentService.registerDepartement(requestBean));
    }

    @Override
    @PostMapping("/room")
    public ResponseEntity<?> registerRoom(@RequestBody RegisterRoomRequestBean requestBean) {
        return handleResponse(departmentService.registerRoom(requestBean));
    }

    @Override
    @PostMapping("/room/bed")
    public ResponseEntity<?> registerBedRoom(@RequestBody RegisterBedRoomRequestBean requestBean) {
        return handleResponse(departmentService.registerBedRoom(requestBean));
    }

    @Override
    @PostMapping("/room/bed/assign")
    public ResponseEntity<?> assignRoomBed(@RequestBody AssignRoomBedRequestBean requestBean) {
        return handleResponse(departmentService.assignBedToPatient(requestBean));
    }

    @Override
    @DeleteMapping("/room/bed/assign")
    public ResponseEntity<?> unassignRoomBed(@RequestBody UnassignRoomBedRequestBean requestBean) {
        return handleResponse(departmentService.unassignBedToPatient(requestBean));
    }
}
