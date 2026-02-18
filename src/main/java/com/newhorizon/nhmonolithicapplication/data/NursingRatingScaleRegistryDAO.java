package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.NursingRatingScaleDTO;

import java.util.List;
import java.util.Optional;

public interface NursingRatingScaleRegistryDAO {
    List<NursingRatingScaleDTO> retrieveAllByMedicalChart(String medicalChartId);

    Optional<NursingRatingScaleDTO> createRecord(NursingRatingScaleDTO build);
}
