package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.AssignTreatmentToTherapyRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CheckTherapyEntryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateSUTRequestBean;
import com.newhorizon.nhmonolithicapplication.service.TherapyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/therapy")
@RequiredArgsConstructor
@Slf4j
public class TherapyControllerImplV1 extends BaseController implements TherapyController {
    private final TherapyService therapyService;

    @Override
    @PostMapping("/sut")
    public ResponseEntity<?> createSUT(@RequestBody CreateSUTRequestBean requestBean) {
        return handleResponse(therapyService.createSUT(requestBean));
    }

    @Override
    @PostMapping
    public ResponseEntity<?> assignTherapy(@RequestBody AssignTreatmentToTherapyRequestBean requestBean) {
        return handleResponse(therapyService.assignTreatmentToSUT(requestBean));
    }

    @Override
    @PostMapping("/check")
    public ResponseEntity<?> checkTherapy(@RequestBody CheckTherapyEntryRequestBean requestBean) {
        return handleResponse(therapyService.checkTreatement(requestBean));
    }
}
