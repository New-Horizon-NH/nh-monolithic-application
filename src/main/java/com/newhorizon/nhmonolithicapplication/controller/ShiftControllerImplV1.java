package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.AutoGenerateMonthlyWorkShiftRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveMonthlyShiftsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateShiftRequestBean;
import com.newhorizon.nhmonolithicapplication.service.WorkShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/shift")
@RequiredArgsConstructor
@Slf4j
public class ShiftControllerImplV1 extends BaseController implements ShiftController {
    private final WorkShiftService workShiftService;

    @Override
    @PostMapping("/generate")
    public ResponseEntity<?> generateMonthlyShift(@RequestBody AutoGenerateMonthlyWorkShiftRequestBean requestBean) {
        return handleResponse(workShiftService.autogenerateWorkShift(requestBean));
    }

    @Override
    @PutMapping
    public ResponseEntity<?> updateShift(@RequestBody UpdateShiftRequestBean requestBean) {
        return handleResponse(workShiftService.updateShift(requestBean));
    }

    @Override
    @GetMapping
    public ResponseEntity<?> retrieveMonthlyShifts(RetrieveMonthlyShiftsRequestBean requestBean) {
        return handleResponse(workShiftService.retrieveMonthlyShifts(requestBean));
    }
}
