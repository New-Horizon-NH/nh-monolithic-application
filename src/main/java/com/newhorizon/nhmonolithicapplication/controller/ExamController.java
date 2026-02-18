package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.AddResultToExamRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateExamMachineRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateExamTypeRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.PrescribePatientExamRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveExamResultsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrievePatientExamRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateExamMachineStatusRequestBean;
import org.springframework.http.ResponseEntity;

public interface ExamController {
    /**
     * Retrieve specific exam info with results
     *
     * @param requestBean request
     * @return Entity with managed response of {@link com.newhorizon.nhmonolithicapplication.beans.response.RetrievePatientExamResponseBean}
     */
    ResponseEntity<?> retrieveSpecificPatientExam(RetrievePatientExamRequestBean requestBean);

    /**
     * Prescribe specific exam to a patient
     *
     * @param requestBean request
     * @return Entity with managed response of {@link com.newhorizon.nhmonolithicapplication.beans.response.PrescribePatientExamResponseBean}
     */
    ResponseEntity<?> prescribeSpecificPatientExam(PrescribePatientExamRequestBean requestBean);

    /**
     * Add result to specific exam
     *
     * @param requestBean request
     * @return Entity with managed response of {@link com.newhorizon.nhmonolithicapplication.beans.response.AddResultToExamResponseBean}
     */
    ResponseEntity<?> addResultToExam(AddResultToExamRequestBean requestBean);

    /**
     * Get result to specific exam
     *
     * @param requestBean request
     * @return Entity with managed response of {@link com.newhorizon.nhmonolithicapplication.beans.response.RetrieveExamResultsResponseBean}
     */
    ResponseEntity<?> retrieveExamResult(RetrieveExamResultsRequestBean requestBean);

    /**
     * Create exam type
     *
     * @param requestBean request
     * @return Entity with managed response of {@link com.newhorizon.nhmonolithicapplication.beans.response.CreateExamTypeResponseBean}
     */
    ResponseEntity<?> createExamType(CreateExamTypeRequestBean requestBean);

    /**
     * Create exam machine
     *
     * @param requestBean request
     * @return Entity with managed response of {@link com.newhorizon.nhmonolithicapplication.beans.response.CreateExamMachineResponseBean}
     */
    ResponseEntity<?> createExamMachine(CreateExamMachineRequestBean requestBean);

    /**
     * Update machine
     *
     * @param requestBean request
     * @return Entity with managed response of {@link com.newhorizon.nhmonolithicapplication.beans.response.UpdateExamMachineStatusResponseBean}
     */
    ResponseEntity<?> updateMachine(UpdateExamMachineStatusRequestBean requestBean);
}
