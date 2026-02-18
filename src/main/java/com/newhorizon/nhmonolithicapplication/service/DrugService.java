package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.CreateActiveIngredientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateDrugActiveIngredientAssociationRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateDrugPackageRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterDrugRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateDrugFornitureRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.WithdrawDrugRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;

public interface DrugService {
    <T extends BaseResponse> T createActiveIngredient(CreateActiveIngredientRequestBean requestBean);

    <T extends BaseResponse> T idempotentCreateActiveIngredient(CreateActiveIngredientRequestBean requestBean);

    <T extends BaseResponse> T createAssociation(CreateDrugActiveIngredientAssociationRequestBean requestBean);

    <T extends BaseResponse> T idempotentCreateAssociation(CreateDrugActiveIngredientAssociationRequestBean requestBean);

    <T extends BaseResponse> T createDrug(RegisterDrugRequestBean requestBean);

    <T extends BaseResponse> T createDrugPackage(CreateDrugPackageRequestBean requestBean);

    <T extends BaseResponse> T withdrawDrugPackage(WithdrawDrugRequestBean requestBean);

    <T extends BaseResponse> T updateDrugForniture(UpdateDrugFornitureRequestBean requestBean);
}
