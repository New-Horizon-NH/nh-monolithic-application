package com.newhorizon.nhmonolithicapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class CalendarEventDTO {
    private String id;
    private String calendarId;
    private String eventTitle;
    private String eventDescription;
    private Instant startDate;
    private Instant endDate;
    private Boolean isEntireDay;
}
