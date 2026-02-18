package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.CreateCalendarRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateEventRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveMyCalendarEventsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveMyCalendarsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateCalendarResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateEventResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrieveMyCalendarEventsResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrieveMyCalendarResponseBean;
import com.newhorizon.nhmonolithicapplication.data.CalendarDAO;
import com.newhorizon.nhmonolithicapplication.data.CalendarEventDAO;
import com.newhorizon.nhmonolithicapplication.data.MedicalDAO;
import com.newhorizon.nhmonolithicapplication.data.PatientDAO;
import com.newhorizon.nhmonolithicapplication.data.PatientEventAssociationDAO;
import com.newhorizon.nhmonolithicapplication.dto.CalendarDTO;
import com.newhorizon.nhmonolithicapplication.dto.CalendarEventDTO;
import com.newhorizon.nhmonolithicapplication.dto.MedicalDTO;
import com.newhorizon.nhmonolithicapplication.dto.PatientEventAssociationDTO;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.Instant;
import java.util.Optional;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CalendarServiceImpl implements CalendarService {
    private final PatientDAO patientDAO;
    private final PatientEventAssociationDAO patientEventAssociationDAO;
    private final MedicalDAO medicalDAO;
    private final CalendarDAO calendarDAO;
    private final CalendarEventDAO calendarEventDAO;

    @Override
    @SuppressWarnings("unchecked")
    public CreateCalendarResponseBean createCalendar(CreateCalendarRequestBean requestBean) {
        Optional<MedicalDTO> medicalDTO = medicalDAO.retrieveByFiscalCode(requestBean.getMedicalFiscalCode());
        if (medicalDTO.isEmpty()) {
            return CreateCalendarResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getDescription())
                    .build();
        }
        if (calendarDAO.findByMedicalIdAndCalendarName(medicalDTO.get().getMemberId(), requestBean.getCalendarName()).isPresent()) {
            return CreateCalendarResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_CALENDAR_ALREADY_EXISTS.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_CALENDAR_ALREADY_EXISTS.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_CALENDAR_ALREADY_EXISTS.getDescription())
                    .build();
        }
        Optional<CalendarDTO> calendarDTO = calendarDAO.createCalendar(CalendarDTO.builder()
                .medicalFiscalCode(requestBean.getMedicalFiscalCode())
                .calendarName(requestBean.getCalendarName())
                .calendarDescription(requestBean.getDescription())
                .build());
        if (calendarDTO.isEmpty()) {
            return CreateCalendarResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return CreateCalendarResponseBean.builder()
                .calendarId(calendarDTO.get().getCalendarId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CreateEventResponseBean createEvent(CreateEventRequestBean requestBean) {
        if (calendarDAO.retrieveById(requestBean.getCalendarId()).isEmpty()) {
            return CreateEventResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_CALENDAR_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_CALENDAR_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_CALENDAR_NOT_FOUND.getDescription())
                    .build();
        }
        if (Boolean.FALSE.equals(calendarEventDAO.findOverlappingEvents(requestBean.getCalendarId(),
                requestBean.getStartDate(),
                requestBean.getEndDate()).isEmpty())) {
            return CreateEventResponseBean.builder()
                    .status(ResponseCodesEnum.SCHEDULING_CONFLICT.getStatus())
                    .responseCode(ResponseCodesEnum.SCHEDULING_CONFLICT.getErrorCode())
                    .responseMessage(ResponseCodesEnum.SCHEDULING_CONFLICT.getDescription())
                    .build();
        }
        if (patientDAO.findPatientByFiscalCode(requestBean.getPatientFiscalCode()).isEmpty()) {
            return CreateEventResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<CalendarEventDTO> calendarEventDTO = calendarEventDAO.scheduleEvent(CalendarEventDTO.builder()
                .calendarId(requestBean.getCalendarId())
                .eventTitle(requestBean.getTitle())
                .eventDescription(requestBean.getDescription())
                .startDate(requestBean.getStartDate())
                .endDate(requestBean.getEndDate())
                .isEntireDay(requestBean.getIsFullDay())
                .build());
        if (calendarEventDTO.isEmpty()) {
            return CreateEventResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        Optional<PatientEventAssociationDTO> associationDTO = patientEventAssociationDAO.createAssociation(PatientEventAssociationDTO.builder()
                .eventId(calendarEventDTO.get().getId())
                .patientFiscalCode(requestBean.getPatientFiscalCode())
                .build());
        if (associationDTO.isEmpty()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CreateEventResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return CreateEventResponseBean.builder()
                .eventId(calendarEventDTO.get().getId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RetrieveMyCalendarResponseBean retrieveMyCalendars(RetrieveMyCalendarsRequestBean requestBean) {
        Optional<MedicalDTO> medicalDTO = medicalDAO.retrieveById(requestBean.getMedicalId());
        if (medicalDTO.isEmpty()) {
            return RetrieveMyCalendarResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getDescription())
                    .build();
        }
        return RetrieveMyCalendarResponseBean.builder()
                .calendarList(calendarDAO.retrieveByMedicalFiscalCode(medicalDTO.get().getFiscalCode())
                        .stream()
                        .map(CalendarDTO::getCalendarId)
                        .toList())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RetrieveMyCalendarEventsResponseBean retrieveCalendarEvents(RetrieveMyCalendarEventsRequestBean requestBean) {
        Optional<MedicalDTO> medicalDTO = medicalDAO.retrieveById(requestBean.getMedicalId());
        if (medicalDTO.isEmpty()) {
            return RetrieveMyCalendarEventsResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<CalendarDTO> calendarDTO = calendarDAO.retrieveById(requestBean.getCalendarId());
        if (calendarDTO.isEmpty()) {
            return RetrieveMyCalendarEventsResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_CALENDAR_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_CALENDAR_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_CALENDAR_NOT_FOUND.getDescription())
                    .build();
        }
        if (Boolean.FALSE.equals(calendarDTO.get().getMedicalFiscalCode().equals(medicalDTO.get().getFiscalCode()))) {
            return RetrieveMyCalendarEventsResponseBean.builder()
                    .status(ResponseCodesEnum.CALENDAR_DO_NOT_BELONG_TO_USER.getStatus())
                    .responseCode(ResponseCodesEnum.CALENDAR_DO_NOT_BELONG_TO_USER.getErrorCode())
                    .responseMessage(ResponseCodesEnum.CALENDAR_DO_NOT_BELONG_TO_USER.getDescription())
                    .build();
        }
        return RetrieveMyCalendarEventsResponseBean.builder()
                .eventList(calendarEventDAO.retrieveEventsFromCalendar(requestBean.getCalendarId(),
                                Instant.now())
                        .stream()
                        .map(CalendarEventDTO::getId)
                        .toList())
                .build();
    }
}
