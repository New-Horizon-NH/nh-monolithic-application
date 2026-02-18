package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.CreateCalendarRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateEventRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveMyCalendarEventsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveMyCalendarsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;

public interface CalendarService {
    <T extends BaseResponse> T createCalendar(CreateCalendarRequestBean requestBean);

    <T extends BaseResponse> T createEvent(CreateEventRequestBean requestBean);

    <T extends BaseResponse> T retrieveMyCalendars(RetrieveMyCalendarsRequestBean requestBean);

    <T extends BaseResponse> T retrieveCalendarEvents(RetrieveMyCalendarEventsRequestBean requestBean);
}
