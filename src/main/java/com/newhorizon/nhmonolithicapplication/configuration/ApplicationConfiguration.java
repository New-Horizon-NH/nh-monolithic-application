package com.newhorizon.nhmonolithicapplication.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.newhorizon.nhmonolithicapplication.enums.WorkShiftEnum;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.CalendarEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.CalendarEventEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.WorkShiftEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.WorkShiftRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfiguration {
    private final JpaRepository<CalendarEventEntity, String> calendarEventRepo;
    private final JpaRepository<CalendarEntity, String> calendarRepo;
    private final WorkShiftRepo workShiftRepo;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.findAndRegisterModules();
        log.debug("Init ObjectMapper");
        return objectMapper;
    }

    @PostConstruct
    void configureShifts() {
        Arrays.stream(WorkShiftEnum.values())
                .forEach(workShiftEnum -> {
                    if (workShiftRepo.findByShiftCode(workShiftEnum.getWorkShiftCode()).isEmpty()) {
                        workShiftRepo.saveAndFlush(WorkShiftEntity.builder()
                                .name(workShiftEnum.name())
                                .shiftCode(workShiftEnum.getWorkShiftCode())
                                .startTime(workShiftEnum.getStartTime())
                                .endTime(workShiftEnum.getEndTime())
                                .build());
                    }
                });
    }

}
