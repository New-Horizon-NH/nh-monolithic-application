package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.GenerateMedicalChartRequestBean;
import com.newhorizon.nhmonolithicapplication.service.MedicalChartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/chart")
@RequiredArgsConstructor
@Slf4j
public class MedicalChartControllerImplV1 extends BaseController implements MedicalChartController {
    private final MedicalChartService medicalChartService;

    @Override
    @PostMapping
    public ResponseEntity<?> generateMedicalChart(@RequestBody GenerateMedicalChartRequestBean requestBean) {
        return handleResponse(medicalChartService.generateMedicalChart(requestBean));
    }
}
