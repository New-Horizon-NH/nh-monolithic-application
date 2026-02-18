package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.GenerateMedicalChartRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterERDocumentRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;

public interface MedicalChartService {
    <T extends BaseResponse> T generateMedicalChart(GenerateMedicalChartRequestBean requestBean);

    <T extends BaseResponse> T uploadERDocument(RegisterERDocumentRequestBean requestBean);
}
