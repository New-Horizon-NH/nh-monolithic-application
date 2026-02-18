package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.CalendarEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface CalendarEventRepo extends JpaRepository<CalendarEventEntity, String> {
    List<CalendarEventEntity> findByCalendarIdAndStartDateAfter(String calendarId, Instant startDate);

    @Query("SELECT event " +
            "FROM CalendarEventEntity event " +
            "JOIN PatientEventAssociationEntity association ON association.eventId=event.id " +
            "JOIN PatientEntity patient ON patient.id=association.patientId " +
            "WHERE patient.patientFiscalCode=:patientFiscalCode AND " +
            "event.startDate>=:now")
    List<CalendarEventEntity> findByPatient(String patientFiscalCode, Instant now);

    @Query("SELECT event " +
            "FROM CalendarEventEntity event " +
            "WHERE event.calendarId = :calendarId " +
            "  AND ((:start <= event.startDate AND :end >= event.endDate) " +
            "    OR (:start <= event.startDate AND " +
            "        (:end >= event.startDate AND :end <= event.endDate)) " +
            "    OR ((:start >= event.startDate AND :start <= event.endDate) AND " +
            "        (:end >= event.startDate AND :end <= event.endDate)) " +
            "    OR ((:start >= event.startDate AND :start <= event.endDate) AND " +
            "        :end >= event.endDate))")
    List<CalendarEventEntity> findOverlappingEvents(String calendarId,
                                                    Instant start,
                                                    Instant end);
}
