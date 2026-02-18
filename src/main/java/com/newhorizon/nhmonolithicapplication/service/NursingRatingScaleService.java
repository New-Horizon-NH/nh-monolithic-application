package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.RegisterScaleRecordRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveScaleListRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;

public interface NursingRatingScaleService {
    <T extends BaseResponse> T createRecord(RegisterScaleRecordRequestBean requestBean);

    <T extends BaseResponse> T retrieveMedicalChartScaleList(RetrieveScaleListRequestBean requestBean);

}
