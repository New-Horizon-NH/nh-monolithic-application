package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.CalendarEventGuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarEventGuestRepo extends JpaRepository<CalendarEventGuestEntity, String> {
}
