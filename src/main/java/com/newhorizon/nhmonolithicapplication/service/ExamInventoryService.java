package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.CreateExamMachineRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateExamTypeRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateExamMachineStatusRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;

public interface ExamInventoryService {
    <T extends BaseResponse> T createNewExamType(CreateExamTypeRequestBean requestBean);

    <T extends BaseResponse> T createNewExamMachine(CreateExamMachineRequestBean requestBean);


    <T extends BaseResponse> T updateMachineStatus(UpdateExamMachineStatusRequestBean requestBean);

}
