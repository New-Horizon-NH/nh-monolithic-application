package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.RegisterERDocumentRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;
import com.newhorizon.nhmonolithicapplication.service.MedicalChartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/er")
@RequiredArgsConstructor
@Slf4j
public class EmergencyRoomControllerImplV1 extends BaseController implements EmergencyRoomController {
    private final MedicalChartService medicalChartService;

    @Override
    @PostMapping("/document")
    public ResponseEntity<? extends BaseResponse> registerDocument(RegisterERDocumentRequestBean requestBean) {
        return handleResponse(medicalChartService.uploadERDocument(requestBean));
    }
}
