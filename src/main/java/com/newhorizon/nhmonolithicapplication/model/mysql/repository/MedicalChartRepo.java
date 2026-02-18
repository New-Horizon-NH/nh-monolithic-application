package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.MedicalChartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MedicalChartRepo extends JpaRepository<MedicalChartEntity, String> {

    @Query("SELECT chart " +
            "FROM MedicalChartEntity chart " +
            "JOIN PatientEntity patient ON patient.id=chart.patientId " +
            "WHERE chart.closingDate IS NULL AND " +
            "UPPER(patient.patientFiscalCode)=UPPER(:patientFiscalCode)")
    Optional<MedicalChartEntity> findActiveMedicalChart(String patientFiscalCode);

    @Query("SELECT chart " +
            "FROM MedicalChartEntity chart " +
            "JOIN MedicalChartMedicalTeamAssociationEntity assoc ON chart.id=assoc.medicalChartId " +
            "JOIN MedicalTeamEntity team ON assoc.medicalTeamId=team.id " +
            "WHERE chart.closingDate IS NULL AND " +
            "UPPER(team.fiscalCode)=UPPER(:medicalFiscalCode)")
    List<MedicalChartEntity> findFollowedByMedicalFiscalCode(String medicalFiscalCode);
}
