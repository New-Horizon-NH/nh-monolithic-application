package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CalendarRepo extends JpaRepository<CalendarEntity, String> {
    Optional<CalendarEntity> findByMedicalIdAndTitleIgnoreCase(String id, String title);

    @Query("SELECT calendar " +
            "FROM CalendarEntity calendar " +
            "JOIN MedicalEntity medical ON medical.id=calendar.medicalId " +
            "WHERE medical.fiscalCode=:fiscalCode")
    List<CalendarEntity> retrieveByMedicalFiscalCode(String fiscalCode);
}
