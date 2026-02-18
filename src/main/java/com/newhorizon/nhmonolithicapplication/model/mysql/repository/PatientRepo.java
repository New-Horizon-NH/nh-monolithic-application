package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PatientRepo extends JpaRepository<PatientEntity, String> {
    Optional<PatientEntity> findByPatientFiscalCode(String fiscalCode);

    @Query("SELECT patient " +
            "FROM PatientEntity patient " +
            "JOIN MedicalChartEntity chart ON chart.patientId=patient.id " +
            "JOIN ExamEntity exam ON exam.medicalChartId=chart.id " +
            "WHERE exam.id=:examCode")
    Optional<PatientEntity> findByExamCode(String examCode);
}
