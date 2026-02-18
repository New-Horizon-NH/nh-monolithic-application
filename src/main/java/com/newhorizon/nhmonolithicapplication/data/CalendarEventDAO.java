package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.CalendarEventDTO;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CalendarEventDAO {
    List<CalendarEventDTO> findOverlappingEvents(String calendarId, Instant startDate, Instant endDate);

    Optional<CalendarEventDTO> scheduleEvent(CalendarEventDTO build);

    List<CalendarEventDTO> retrieveEventsFromCalendar(String calendarId, Instant startDate);

    List<CalendarEventDTO> retrieveEventsByPatient(String patientFiscalCode);

}
