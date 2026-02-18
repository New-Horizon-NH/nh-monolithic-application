package com.newhorizon.nhmonolithicapplication.beans.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class CreateEventRequestBean {
    private String calendarId;
    private String title;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private Boolean isFullDay;
    private String patientFiscalCode;
}
