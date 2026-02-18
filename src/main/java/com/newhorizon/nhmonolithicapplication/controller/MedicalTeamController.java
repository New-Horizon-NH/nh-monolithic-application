package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.AddMedicalTeamMemberRequestBean;
import org.springframework.http.ResponseEntity;

public interface MedicalTeamController {
    /**
     * Create a new medical member in the system
     *
     * @param requestBean request
     * @return Entity with managed response of {@link com.newhorizon.nhmonolithicapplication.beans.response.AddMedicalTeamMemberResponseBean}
     */
    ResponseEntity<?> createMember(AddMedicalTeamMemberRequestBean requestBean);
}
