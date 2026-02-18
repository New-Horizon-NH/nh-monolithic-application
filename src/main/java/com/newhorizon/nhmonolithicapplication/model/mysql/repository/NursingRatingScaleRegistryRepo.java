package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.NursingRatingScaleRegistryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NursingRatingScaleRegistryRepo extends JpaRepository<NursingRatingScaleRegistryEntity, String> {
    List<NursingRatingScaleRegistryEntity> findByMedicalChartId(String medicalChartId);
}
