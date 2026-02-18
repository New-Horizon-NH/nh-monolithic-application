package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.CalendarDTO;

import java.util.List;
import java.util.Optional;

public interface CalendarDAO {
    Optional<CalendarDTO> findByMedicalIdAndCalendarName(String memberId, String calendarName);

    Optional<CalendarDTO> createCalendar(CalendarDTO build);

    Optional<CalendarDTO> retrieveById(String calendarId);

    List<CalendarDTO> retrieveByMedicalFiscalCode(String medicalFiscalCode);

}
