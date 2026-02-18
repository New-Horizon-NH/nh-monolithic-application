package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.AddMedicalTeamMemberRequestBean;
import com.newhorizon.nhmonolithicapplication.service.MedicalTeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/medical")
@RequiredArgsConstructor
@Slf4j
public class MedicalTeamControllerImplV1 extends BaseController implements MedicalTeamController {
    private final MedicalTeamService medicalTeamService;

    @Override
    @PostMapping("/member")
    public ResponseEntity<?> createMember(@RequestBody AddMedicalTeamMemberRequestBean requestBean) {
        return handleResponse(medicalTeamService.addMember(requestBean));
    }
}
