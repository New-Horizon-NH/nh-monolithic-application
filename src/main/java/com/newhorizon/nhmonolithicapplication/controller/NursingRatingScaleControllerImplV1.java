package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.RegisterScaleRecordRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveScaleListRequestBean;
import com.newhorizon.nhmonolithicapplication.service.NursingRatingScaleService;
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
@RequestMapping("/v1/nrs")
@RequiredArgsConstructor
@Slf4j
public class NursingRatingScaleControllerImplV1 extends BaseController implements NursingRatingScaleController {
    private final NursingRatingScaleService nursingRatingScaleService;

    @Override
    @PostMapping
    public ResponseEntity<?> registerScale(@RequestBody RegisterScaleRecordRequestBean requestBean) {
        return handleResponse(nursingRatingScaleService.createRecord(requestBean));
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<?> retrieveScale(@ModelAttribute RetrieveScaleListRequestBean requestBean) {
        return handleResponse(nursingRatingScaleService.retrieveMedicalChartScaleList(requestBean));
    }
}
