package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.AssignTreatmentToTherapyRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CheckTherapyEntryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateSUTRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;

public interface TherapyService {
    <T extends BaseResponse> T createSUT(CreateSUTRequestBean requestBean);

    <T extends BaseResponse> T assignTreatmentToSUT(AssignTreatmentToTherapyRequestBean requestBean);

    <T extends BaseResponse> T checkTreatement(CheckTherapyEntryRequestBean requestBean);

}
