package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.AssignMemberToPatientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.GenerateMeetRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterPatientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveCurrentlyFollowedPatientListRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrievePatientAnagraficaRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.GenerateMeetResponseBean;
import com.newhorizon.nhmonolithicapplication.service.MedicalTeamService;
import com.newhorizon.nhmonolithicapplication.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/patient")
@RequiredArgsConstructor
@Slf4j
public class PatientControllerImplV1 extends BaseController implements PatientController {
    private final PatientService patientService;
    private final MedicalTeamService medicalTeamService;

    @Override
    @GetMapping("/myPatientList")
    public ResponseEntity<?> retrieveMyPatientList(@ModelAttribute RetrieveCurrentlyFollowedPatientListRequestBean requestBean) {
        return handleResponse(medicalTeamService.retrieveCurrentlyFollowedPatientList(requestBean));
    }

    @Override
    @PostMapping("/meet")
    public ResponseEntity<?> generateMeet(@RequestBody GenerateMeetRequestBean requestBean) {
        return handleResponse(GenerateMeetResponseBean.builder()
                .url("https://meet.google.com/jfh-yepy-gtp")
                .build());
    }

    @Override
    @PostMapping
    public ResponseEntity<?> registerPatientToSystem(@RequestBody RegisterPatientRequestBean requestBean) {
        return handleResponse(patientService.registerNewPatient(requestBean));
    }

    @Override
    @GetMapping("/anagrafica")
    public ResponseEntity<?> anagrafica(@ModelAttribute RetrievePatientAnagraficaRequestBean requestBean) {
        return handleResponse(patientService.retrieveAnagrafica(requestBean));
    }

    @Override
    @PostMapping("/assignDoctor")
    public ResponseEntity<?> assignMedicalToPatient(@RequestBody AssignMemberToPatientRequestBean requestBean) {
        return handleResponse(medicalTeamService.assignMemberToPatient(requestBean));
    }
}
