package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.AddResultToExamRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateExamMachineRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateExamTypeRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.PrescribePatientExamRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveExamResultsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrievePatientExamRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateExamMachineStatusRequestBean;
import com.newhorizon.nhmonolithicapplication.service.ExamInventoryService;
import com.newhorizon.nhmonolithicapplication.service.ExamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/exam")
@RequiredArgsConstructor
@Slf4j
public class ExamControllerImplV1 extends BaseController implements ExamController {
    private final ExamService examService;
    private final ExamInventoryService examInventoryService;

    @Override
    @GetMapping
    public ResponseEntity<?> retrieveSpecificPatientExam(@ModelAttribute RetrievePatientExamRequestBean requestBean) {
        return handleNotImplemented();
    }

    @Override
    @PostMapping
    public ResponseEntity<?> prescribeSpecificPatientExam(@RequestBody PrescribePatientExamRequestBean requestBean) {
        return handleResponse(examService.prescribeExamToPatient(requestBean));
    }

    @Override
    @PostMapping("/result")
    public ResponseEntity<?> addResultToExam(@RequestBody AddResultToExamRequestBean requestBean) {
        return handleResponse(examService.addResultToExam(requestBean));
    }

    @Override
    @GetMapping("/result")
    public ResponseEntity<?> retrieveExamResult(@ModelAttribute RetrieveExamResultsRequestBean requestBean) {
        return handleResponse(examService.retrieveExamResults(requestBean));
    }

    @Override
    @PostMapping("/inventory/type")
    public ResponseEntity<?> createExamType(@RequestBody CreateExamTypeRequestBean requestBean) {
        return handleResponse(examInventoryService.createNewExamType(requestBean));
    }

    @Override
    @PostMapping("/inventory/machine")
    public ResponseEntity<?> createExamMachine(@RequestBody CreateExamMachineRequestBean requestBean) {
        return handleResponse(examInventoryService.createNewExamMachine(requestBean));
    }

    @Override
    @PutMapping("/inventory/machine")
    public ResponseEntity<?> updateMachine(@RequestBody UpdateExamMachineStatusRequestBean requestBean) {
        return handleResponse(examInventoryService.updateMachineStatus(requestBean));
    }
}
