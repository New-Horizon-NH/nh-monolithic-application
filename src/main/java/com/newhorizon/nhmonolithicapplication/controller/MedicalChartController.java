package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.GenerateMedicalChartRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.GenerateMedicalChartResponseBean;
import org.springframework.http.ResponseEntity;

public interface MedicalChartController {
    /**
     * Generate new medical chart related to patient
     *
     * @param requestBean request
     * @return Entity with managed response of {@link GenerateMedicalChartResponseBean}
     */
    ResponseEntity<?> generateMedicalChart(GenerateMedicalChartRequestBean requestBean);
}
