package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.CreateDrugPackageRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterDrugRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateDrugFornitureRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.WithdrawDrugRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateDrugPackageResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterDrugResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UpdateDrugFornitureResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.WithdrawDrugResponseBean;
import org.springframework.http.ResponseEntity;

public interface PharmacyController {
    /**
     * Register drug with quantity inside pharmacy
     *
     * @param requestBean request
     * @return Entity with managed response of {@link RegisterDrugResponseBean}
     */
    ResponseEntity<? extends BaseResponse> registerDrug(RegisterDrugRequestBean requestBean);

    /**
     * Register drug with quantity inside pharmacy
     *
     * @param requestBean request
     * @return Entity with managed response of {@link CreateDrugPackageResponseBean}
     */
    ResponseEntity<? extends BaseResponse> registerDrugPackage(CreateDrugPackageRequestBean requestBean);

    /**
     * Withdraw specific drug with quantity
     *
     * @param requestBean request
     * @return Entity with managed response of {@link WithdrawDrugResponseBean}
     */
    ResponseEntity<? extends BaseResponse> withdrawDrug(WithdrawDrugRequestBean requestBean);

    /**
     * Update specific drug
     *
     * @param requestBean request
     * @return Entity with managed response of {@link UpdateDrugFornitureResponseBean}
     */
    ResponseEntity<? extends BaseResponse> updateDrug(UpdateDrugFornitureRequestBean requestBean);

}
