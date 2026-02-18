package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.CreateDrugPackageRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterDrugRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateDrugFornitureRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.WithdrawDrugRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;
import com.newhorizon.nhmonolithicapplication.service.DrugService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/pharmacy")
@RequiredArgsConstructor
@Slf4j
public class PharmacyControllerImplV1 extends BaseController implements PharmacyController {
    private final DrugService drugService;

    @PostMapping("/drug")
    @Override
    public ResponseEntity<? extends BaseResponse> registerDrug(@RequestBody RegisterDrugRequestBean requestBean) {
        return handleResponse(drugService.createDrug(requestBean));
    }

    @Override
    @PostMapping("/drug/package")
    public ResponseEntity<? extends BaseResponse> registerDrugPackage(@RequestBody CreateDrugPackageRequestBean requestBean) {
        return handleResponse(drugService.createDrugPackage(requestBean));
    }

    @Override
    @PostMapping("/withdraw")
    public ResponseEntity<? extends BaseResponse> withdrawDrug(@RequestBody WithdrawDrugRequestBean requestBean) {
        return handleResponse(drugService.withdrawDrugPackage(requestBean));
    }

    @Override
    @PutMapping("/drug")
    public ResponseEntity<? extends BaseResponse> updateDrug(@RequestBody UpdateDrugFornitureRequestBean requestBean) {
        return handleResponse(drugService.updateDrugForniture(requestBean));
    }
}
