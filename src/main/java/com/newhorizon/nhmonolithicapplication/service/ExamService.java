package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.AddResultToExamRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.PrescribePatientExamRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveExamResultsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnprescribePatientExamRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;

public interface ExamService {
    <T extends BaseResponse> T prescribeExamToPatient(PrescribePatientExamRequestBean requestBean);

    <T extends BaseResponse> T unprescribeExamToPatient(UnprescribePatientExamRequestBean requestBean);

    <T extends BaseResponse> T addResultToExam(AddResultToExamRequestBean requestBean);

    <T extends BaseResponse> T retrieveExamResults(RetrieveExamResultsRequestBean requestBean);
}
