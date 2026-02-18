package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.CalendarEventDTO;
import com.newhorizon.nhmonolithicapplication.mapper.CalendarEventMapper;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.CalendarEventRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class CalendarEventRepository implements CalendarEventDAO {
    private final CalendarEventRepo calendarEventRepo;
    private final CalendarEventMapper calendarEventMapper;

    @Override
    public List<CalendarEventDTO> findOverlappingEvents(String calendarId, Instant startDate, Instant endDate) {
        return calendarEventRepo.findOverlappingEvents(calendarId,
                        startDate,
                        endDate)
                .stream()
                .map(calendarEventMapper::parseEntity)
                .toList();
    }

    @Override
    public Optional<CalendarEventDTO> scheduleEvent(CalendarEventDTO build) {
        return Optional.of(calendarEventMapper.parseEntity(
                calendarEventRepo.saveAndFlush(
                        calendarEventMapper.parseDTO(build)
                )
        ));
    }

    @Override
    public List<CalendarEventDTO> retrieveEventsFromCalendar(String calendarId, Instant startDate) {
        return calendarEventRepo.findByCalendarIdAndStartDateAfter(calendarId, startDate)
                .stream()
                .map(calendarEventMapper::parseEntity)
                .toList();
    }

    @Override
    public List<CalendarEventDTO> retrieveEventsByPatient(String patientFiscalCode) {
        return calendarEventRepo.findByPatient(patientFiscalCode, Instant.now())
                .stream()
                .map(calendarEventMapper::parseEntity)
                .toList();
    }
}
