package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.CreateCalendarRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateEventRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveMyCalendarEventsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveMyCalendarsRequestBean;
import com.newhorizon.nhmonolithicapplication.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/calendar")
@RequiredArgsConstructor
@Slf4j
public class CalendarControllerImplV1 extends BaseController implements CalendarController {
    private final CalendarService calendarService;

    @Override
    @PostMapping
    public ResponseEntity<?> createCalendar(@RequestBody CreateCalendarRequestBean requestBean) {
        return handleResponse(calendarService.createCalendar(requestBean));
    }

    @Override
    @PostMapping("/event")
    public ResponseEntity<?> createEvent(@RequestBody CreateEventRequestBean requestBean) {
        return handleResponse(calendarService.createEvent(requestBean));
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<?> myCalendar(@ModelAttribute RetrieveMyCalendarsRequestBean requestBean) {
        return handleResponse(calendarService.retrieveMyCalendars(requestBean));
    }

    @Override
    @GetMapping("/events")
    public ResponseEntity<?> myEvent(@ModelAttribute RetrieveMyCalendarEventsRequestBean requestBean) {
        return handleResponse(calendarService.retrieveCalendarEvents(requestBean));
    }
}
