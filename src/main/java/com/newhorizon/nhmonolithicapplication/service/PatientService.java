package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.RegisterPatientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrievePatientAnagraficaRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;

public interface PatientService {
    <T extends BaseResponse> T retrieveAnagrafica(RetrievePatientAnagraficaRequestBean requestBean);

    <T extends BaseResponse> T registerNewPatient(RegisterPatientRequestBean requestBean);
}
