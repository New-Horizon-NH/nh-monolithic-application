package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.MedicalChartDTO;

import java.util.List;
import java.util.Optional;

public interface MedicalChartDAO {
    Optional<MedicalChartDTO> retrieveOpenMedicalChart(String patientFiscalCode);

    Optional<MedicalChartDTO> openMedicalChart(String patientFiscalCode);

    Optional<MedicalChartDTO> retrieveMedicalChart(String medicalChartId);

    List<MedicalChartDTO> retrieveFollowedByHospitalMemberFiscalCode(String loggedMemberFiscalCode);

}
