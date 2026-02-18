package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.NhMonolithicApplicationTest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Transactional
class CalendarServiceTest extends NhMonolithicApplicationTest {
    @Autowired
    CalendarServiceImpl calendarService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createCalendar() {
    }

    @Test
    void createEvent() {
    }

    @Test
    void retrieveMyCalendars() {
    }

    @Test
    void retrieveCalendarEvents() {
    }
}