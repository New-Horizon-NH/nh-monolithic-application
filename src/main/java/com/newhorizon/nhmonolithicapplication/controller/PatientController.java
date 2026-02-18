package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.AssignMemberToPatientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.GenerateMeetRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterPatientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveCurrentlyFollowedPatientListRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrievePatientAnagraficaRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.GenerateMeetResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterPatientResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrieveCurrentlyFollowedPatientListResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrievePatientAnagraficaResponseBean;
import org.springframework.http.ResponseEntity;

public interface PatientController {
    /**
     * Retrieve the list of current followed patients by logged user that have to be a doctor only
     *
     * @param requestBean request
     * @return Entity with managed response of {@link RetrieveCurrentlyFollowedPatientListResponseBean}
     */
    ResponseEntity<?> retrieveMyPatientList(RetrieveCurrentlyFollowedPatientListRequestBean requestBean);

    /**
     * Generate new meet with a patient or a user for a specific prestazione including telemedicina link.
     * This involves the generation of event in user and doctor calendar
     *
     * @param requestBean request
     * @return Entity with managed response of {@link GenerateMeetResponseBean}
     */
    ResponseEntity<?> generateMeet(GenerateMeetRequestBean requestBean);

    /**
     * Register the patient inside the system if never registered
     *
     * @param requestBean request
     * @return Entity with managed response of {@link RegisterPatientResponseBean}
     */
    ResponseEntity<?> registerPatientToSystem(RegisterPatientRequestBean requestBean);

    /**
     * Retrieve patient anagrafica from fiscal code
     *
     * @param requestBean request
     * @return Entity with managed response of {@link RetrievePatientAnagraficaResponseBean}
     */
    ResponseEntity<?> anagrafica(RetrievePatientAnagraficaRequestBean requestBean);

    /**
     * Assign medical to patient
     *
     * @param requestBean request
     * @return Entity with managed response of {@link com.newhorizon.nhmonolithicapplication.beans.response.AssignMemberToPatientResponseBean}
     */
    ResponseEntity<?> assignMedicalToPatient(AssignMemberToPatientRequestBean requestBean);
}
