package com.newhorizon.nhmonolithicapplication.mapper;

import com.newhorizon.nhmonolithicapplication.dto.CalendarEventDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.CalendarEventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CalendarEventMapper {
    @Mapping(source = "eventTitle", target = "title")
    CalendarEventEntity parseDTO(CalendarEventDTO dto);

    @Mapping(source = "title", target = "eventTitle")
    CalendarEventDTO parseEntity(CalendarEventEntity entity);
}
