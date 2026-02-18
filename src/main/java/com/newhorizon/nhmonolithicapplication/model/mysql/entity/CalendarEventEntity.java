package com.newhorizon.nhmonolithicapplication.model.mysql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CalendarEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String calendarId;
    @Column(nullable = false)
    private String title;
    private String eventDescription;
    @Column(nullable = false)
    private Instant startDate;
    private Instant endDate;
    @Column(nullable = false,
            columnDefinition = "BIT(1) NOT NULL DAFAULT 0")
    private Boolean isEntireDay;
}
