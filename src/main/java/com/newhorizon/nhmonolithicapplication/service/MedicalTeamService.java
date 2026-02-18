package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.AddMedicalTeamMemberRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.AssignMemberToPatientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveCurrentlyFollowedPatientListRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;

public interface MedicalTeamService {
    <T extends BaseResponse> T addMember(AddMedicalTeamMemberRequestBean requestBean);

    <T extends BaseResponse> T assignMemberToPatient(AssignMemberToPatientRequestBean requestBean);

    <T extends BaseResponse> T retrieveCurrentlyFollowedPatientList(RetrieveCurrentlyFollowedPatientListRequestBean requestBean);
}
