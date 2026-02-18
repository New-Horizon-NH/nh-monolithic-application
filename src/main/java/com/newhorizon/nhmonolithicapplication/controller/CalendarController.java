package com.newhorizon.nhmonolithicapplication.controller;

import com.newhorizon.nhmonolithicapplication.beans.request.CreateCalendarRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateEventRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveMyCalendarEventsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveMyCalendarsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateCalendarResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateEventResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrieveMyCalendarEventsResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrieveMyCalendarResponseBean;
import org.springframework.http.ResponseEntity;

public interface CalendarController {
    /**
     * Create a new calendar for provided user
     *
     * @param requestBean request
     * @return Entity with managed response of {@link CreateCalendarResponseBean}
     */
    ResponseEntity<?> createCalendar(CreateCalendarRequestBean requestBean);

    /**
     * Create a new event in calendar
     *
     * @param requestBean request
     * @return Entity with managed response of {@link CreateEventResponseBean}
     */
    ResponseEntity<?> createEvent(CreateEventRequestBean requestBean);

    /**
     * Retrieve personal calendars
     *
     * @param requestBean request
     * @return Entity with managed response of {@link RetrieveMyCalendarResponseBean}
     */
    ResponseEntity<?> myCalendar(RetrieveMyCalendarsRequestBean requestBean);

    /**
     * Retrieve calendar events
     *
     * @param requestBean request
     * @return Entity with managed response of {@link RetrieveMyCalendarEventsResponseBean}
     */
    ResponseEntity<?> myEvent(RetrieveMyCalendarEventsRequestBean requestBean);
}

