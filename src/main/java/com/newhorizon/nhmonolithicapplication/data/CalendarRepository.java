package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.CalendarDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.CalendarEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.CalendarRepo;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.MedicalRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class CalendarRepository implements CalendarDAO {
    private final CalendarRepo calendarRepo;
    private final MedicalRepo medicalRepo;

    @Override
    public Optional<CalendarDTO> findByMedicalIdAndCalendarName(String memberId, String calendarName) {
        return calendarRepo.findByMedicalIdAndTitleIgnoreCase(memberId, calendarName)
                .map(entity -> CalendarDTO.builder()
                        .calendarId(entity.getId())
                        .medicalFiscalCode(medicalRepo.findById(memberId).orElseThrow().getFiscalCode())
                        .calendarName(entity.getTitle())
                        .calendarDescription(entity.getCalendarDescription())
                        .build());
    }

    @Override
    public Optional<CalendarDTO> createCalendar(CalendarDTO build) {
        CalendarEntity entity = CalendarEntity.builder()
                .medicalId(medicalRepo.findByFiscalCode(build.getMedicalFiscalCode()).orElseThrow().getId())
                .title(build.getCalendarName())
                .calendarDescription(build.getCalendarDescription())
                .build();
        calendarRepo.saveAndFlush(entity);
        return Optional.of(build);
    }

    @Override
    public Optional<CalendarDTO> retrieveById(String calendarId) {
        return calendarRepo.findById(calendarId)
                .map(entity -> CalendarDTO.builder()
                        .calendarId(entity.getId())
                        .medicalFiscalCode(medicalRepo.findById(entity.getMedicalId()).orElseThrow().getFiscalCode())
                        .calendarName(entity.getTitle())
                        .calendarDescription(entity.getCalendarDescription())
                        .build());
    }

    @Override
    public List<CalendarDTO> retrieveByMedicalFiscalCode(String medicalFiscalCode) {
        return calendarRepo.retrieveByMedicalFiscalCode(medicalFiscalCode)
                .stream()
                .map(entity -> CalendarDTO.builder()
                        .calendarId(entity.getId())
                        .medicalFiscalCode(medicalFiscalCode)
                        .calendarName(entity.getTitle())
                        .calendarDescription(entity.getCalendarDescription())
                        .build())
                .toList();
    }
}
