package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.AssignTreatmentToTherapyRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CheckTherapyEntryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateSUTRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.AssignTreatmentToTherapyResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CheckTherapyEntryResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateSUTResponseBean;
import org.springframework.http.ResponseEntity;

public interface TherapyController {
    /**
     * Create new therapy invalidating previous
     *
     * @param requestBean request
     * @return Entity with managed response of {@link CreateSUTResponseBean}
     */
    ResponseEntity<?> createSUT(CreateSUTRequestBean requestBean);

    /**
     * Assign therapy entry to a specific patient in a given medical chart
     *
     * @param requestBean request
     * @return Entity with managed response of {@link AssignTreatmentToTherapyResponseBean}
     */
    ResponseEntity<?> assignTherapy(AssignTreatmentToTherapyRequestBean requestBean);

    /**
     * Check therapy entry with a state that can be DONE, REFUSED, SUSPENDED or other
     *
     * @param requestBean request
     * @return Entity with managed response of {@link CheckTherapyEntryResponseBean}
     */
    ResponseEntity<?> checkTherapy(CheckTherapyEntryRequestBean requestBean);
}
