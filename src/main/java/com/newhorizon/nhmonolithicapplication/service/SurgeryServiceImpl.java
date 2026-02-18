package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.CreateSurgeryRoomRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateSurgeryTypeRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RescheduleSurgeryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.ScheduleSurgeryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnscheduleSurgeryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateSurgeryRoomResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateSurgeryTypeResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RescheduleSurgeryResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.ScheduleSurgeryResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UnscheduleSurgeryResponseBean;
import com.newhorizon.nhmonolithicapplication.data.MedicalChartDAO;
import com.newhorizon.nhmonolithicapplication.data.PatientDAO;
import com.newhorizon.nhmonolithicapplication.data.SurgeryDAO;
import com.newhorizon.nhmonolithicapplication.data.SurgeryTypeDAO;
import com.newhorizon.nhmonolithicapplication.data.SurgicalRoomDAO;
import com.newhorizon.nhmonolithicapplication.dto.MedicalChartDTO;
import com.newhorizon.nhmonolithicapplication.dto.SurgeryDTO;
import com.newhorizon.nhmonolithicapplication.dto.SurgeryTypeDTO;
import com.newhorizon.nhmonolithicapplication.dto.SurgicalRoomDTO;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import com.newhorizon.nhmonolithicapplication.enums.SurgicalRoomTypeEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SurgeryServiceImpl implements SurgeryService {
    private final SurgicalRoomDAO surgicalRoomDAO;
    private final SurgeryTypeDAO surgeryTypeDAO;
    private final SurgeryDAO surgeryDAO;
    private final PatientDAO patientDAO;
    private final MedicalChartDAO medicalChartDAO;

    @Override
    @SuppressWarnings("unchecked")
    public CreateSurgeryRoomResponseBean createSurgeryRoom(CreateSurgeryRoomRequestBean requestBean) {
        if (surgicalRoomDAO.findByRoomNumber(requestBean.getRoomNumber()).isPresent()) {
            return CreateSurgeryRoomResponseBean.builder()
                    .status(ResponseCodesEnum.SURGICAL_ROOM_ALREADY_EXISTS.getStatus())
                    .responseCode(ResponseCodesEnum.SURGICAL_ROOM_ALREADY_EXISTS.getErrorCode())
                    .responseMessage(ResponseCodesEnum.SURGICAL_ROOM_ALREADY_EXISTS.getDescription())
                    .build();
        }
        if (isNull(SurgicalRoomTypeEnum.getTypeByCode(requestBean.getRoomType()))) {
            return CreateSurgeryRoomResponseBean.builder()
                    .status(ResponseCodesEnum.SURGICAL_ROOM_TYPE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.SURGICAL_ROOM_TYPE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.SURGICAL_ROOM_TYPE_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<SurgicalRoomDTO> saved = surgicalRoomDAO.createSurgicalRoom(SurgicalRoomDTO.builder()
                .roomNumber(requestBean.getRoomNumber())
                .roomType(requestBean.getRoomType())
                .build());
        if (saved.isEmpty()) {
            return CreateSurgeryRoomResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return CreateSurgeryRoomResponseBean.builder()
                .roomNumber(saved.get().getRoomNumber())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CreateSurgeryTypeResponseBean createSurgeryType(CreateSurgeryTypeRequestBean requestBean) {
        if (surgeryTypeDAO.findBySurgeryCode(requestBean.getSurgeryTypeCode()).isPresent()) {
            return CreateSurgeryTypeResponseBean.builder()
                    .status(ResponseCodesEnum.SURGERY_TYPE_ALREADY_EXISTS.getStatus())
                    .responseCode(ResponseCodesEnum.SURGERY_TYPE_ALREADY_EXISTS.getErrorCode())
                    .responseMessage(ResponseCodesEnum.SURGERY_TYPE_ALREADY_EXISTS.getDescription())
                    .build();
        }
        Optional<SurgeryTypeDTO> saved = surgeryTypeDAO.createSurgeryType(SurgeryTypeDTO.builder()
                .surgeryTypeCode(requestBean.getSurgeryTypeCode())
                .surgeryTypeName(requestBean.getSurgeryTypeName())
                .surgeryTypeDescription(requestBean.getSurgeryTypeDescription())
                .build());
        if (saved.isEmpty()) {
            return CreateSurgeryTypeResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return CreateSurgeryTypeResponseBean.builder()
                .surgeryTypeCode(saved.get().getSurgeryTypeCode())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public ScheduleSurgeryResponseBean scheduleSurgery(ScheduleSurgeryRequestBean requestBean) {
        if (patientDAO.findPatientByFiscalCode(requestBean.getPatientFiscalCode()).isEmpty()) {
            return ScheduleSurgeryResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<MedicalChartDTO> medicalChartDTO = medicalChartDAO.retrieveOpenMedicalChart(requestBean.getPatientFiscalCode());
        if (medicalChartDTO.isEmpty()) {
            return ScheduleSurgeryResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<SurgeryTypeDTO> surgeryTypeDTO = surgeryTypeDAO.findBySurgeryCode(requestBean.getSurgeryTypeCode());
        if (surgeryTypeDTO.isEmpty()) {
            return ScheduleSurgeryResponseBean.builder()
                    .status(ResponseCodesEnum.SURGERY_TYPE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.SURGERY_TYPE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.SURGERY_TYPE_NOT_FOUND.getDescription())
                    .build();
        }
        if (surgicalRoomDAO.findByRoomNumber(requestBean.getSurgicalRoomNumber()).isEmpty()) {
            return ScheduleSurgeryResponseBean.builder()
                    .status(ResponseCodesEnum.SURGICAL_ROOM_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.SURGICAL_ROOM_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.SURGICAL_ROOM_NOT_FOUND.getDescription())
                    .build();
        }
        if (requestBean.getSurgeryStart().isAfter(requestBean.getSurgeryEnd())) {
            return ScheduleSurgeryResponseBean.builder()
                    .status(ResponseCodesEnum.SURGERY_START_AFTER_END.getStatus())
                    .responseCode(ResponseCodesEnum.SURGERY_START_AFTER_END.getErrorCode())
                    .responseMessage(ResponseCodesEnum.SURGERY_START_AFTER_END.getDescription())
                    .build();
        }
        if (Boolean.FALSE.equals(surgeryDAO.findOverlappingSurgeries(requestBean.getSurgicalRoomNumber(),
                requestBean.getSurgeryStart(),
                requestBean.getSurgeryEnd()).isEmpty())) {
            return ScheduleSurgeryResponseBean.builder()
                    .status(ResponseCodesEnum.SCHEDULING_CONFLICT.getStatus())
                    .responseCode(ResponseCodesEnum.SCHEDULING_CONFLICT.getErrorCode())
                    .responseMessage(ResponseCodesEnum.SCHEDULING_CONFLICT.getDescription())
                    .build();
        }
        Optional<SurgeryDTO> saved = surgeryDAO.scheduleSurgery(SurgeryDTO.builder()
                .surgeryStart(requestBean.getSurgeryStart())
                .surgeryEnd(requestBean.getSurgeryEnd())
                .medicalChartId(medicalChartDTO.get().getMedicalChartId())
                .surgeryTypeCode(requestBean.getSurgeryTypeCode())
                .surgicalRoomNumber(requestBean.getSurgicalRoomNumber())
                .build());
        if (saved.isEmpty()) {
            return ScheduleSurgeryResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return ScheduleSurgeryResponseBean.builder()
                .surgeryId(saved.get().getSurgeryId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public UnscheduleSurgeryResponseBean unscheduleSurgery(UnscheduleSurgeryRequestBean requestBean) {
        if (surgeryDAO.retrieveScheduledSurgery(requestBean.getSurgeryId()).isEmpty()) {
            return UnscheduleSurgeryResponseBean.builder()
                    .status(ResponseCodesEnum.SCHEDULED_SURGERY_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.SCHEDULED_SURGERY_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.SCHEDULED_SURGERY_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<SurgeryDTO> saved = surgeryDAO.unscheduleSurgery(requestBean.getSurgeryId());
        if (saved.isEmpty()) {
            return UnscheduleSurgeryResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return UnscheduleSurgeryResponseBean.builder()
                .surgeryId(saved.get().getSurgeryId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RescheduleSurgeryResponseBean rescheduleSurgery(RescheduleSurgeryRequestBean requestBean) {
        Optional<SurgeryDTO> surgeryDTO = surgeryDAO.retrieveScheduledSurgery(requestBean.getSurgeryId());
        if (surgeryDTO.isEmpty()) {
            return RescheduleSurgeryResponseBean.builder()
                    .status(ResponseCodesEnum.SCHEDULED_SURGERY_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.SCHEDULED_SURGERY_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.SCHEDULED_SURGERY_NOT_FOUND.getDescription())
                    .build();
        }
        long surgeryDuration = Duration.between(surgeryDTO.get().getSurgeryStart(), surgeryDTO.get().getSurgeryEnd()).toMillis();
        Instant newStart = requestBean.getPostponedStart();
        Instant newEnd = requestBean.getPostponedStart().plusMillis(surgeryDuration);
        if (Boolean.FALSE.equals(surgeryDAO.findOverlappingSurgeries(surgeryDTO.get().getSurgicalRoomNumber(),
                newStart,
                newEnd).isEmpty())) {
            return RescheduleSurgeryResponseBean.builder()
                    .status(ResponseCodesEnum.SCHEDULING_CONFLICT.getStatus())
                    .responseCode(ResponseCodesEnum.SCHEDULING_CONFLICT.getErrorCode())
                    .responseMessage(ResponseCodesEnum.SCHEDULING_CONFLICT.getDescription())
                    .build();
        }
        surgeryDTO.get().setSurgeryStart(newStart);
        surgeryDTO.get().setSurgeryEnd(newEnd);
        Optional<SurgeryDTO> saved = surgeryDAO.rescheduleSurgery(surgeryDTO.get());
        if (saved.isEmpty()) {
            return RescheduleSurgeryResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return RescheduleSurgeryResponseBean.builder()
                .surgeryId(saved.get().getSurgeryId())
                .build();
    }
}
