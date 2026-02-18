package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.AssignRoomBedRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterBedRoomRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterDepartmentRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterRoomRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnassignRoomBedRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.AssignRoomBedResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterBedRoomResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterDepartmentResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterRoomResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UnassignRoomBedResponseBean;
import com.newhorizon.nhmonolithicapplication.data.DepartmentDAO;
import com.newhorizon.nhmonolithicapplication.data.DepartmentRoomBedDAO;
import com.newhorizon.nhmonolithicapplication.data.DepartmentRoomDAO;
import com.newhorizon.nhmonolithicapplication.data.PatientDAO;
import com.newhorizon.nhmonolithicapplication.dto.BedDTO;
import com.newhorizon.nhmonolithicapplication.dto.DepartmentDTO;
import com.newhorizon.nhmonolithicapplication.dto.RoomDTO;
import com.newhorizon.nhmonolithicapplication.enums.BedOperationTypeEnum;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentDAO departmentDAO;
    private final DepartmentRoomDAO departmentRoomDAO;
    private final DepartmentRoomBedDAO departmentRoomBedDAO;
    private final PatientDAO patientDAO;

    @Override
    @SuppressWarnings("unchecked")
    public RegisterDepartmentResponseBean registerDepartement(RegisterDepartmentRequestBean requestBean) {
        if (departmentDAO.retrieveByCode(requestBean.getCode()).isPresent()) {
            return RegisterDepartmentResponseBean.builder()
                    .status(ResponseCodesEnum.DEPARTMENT_ALREADY_EXISTS.getStatus())
                    .responseCode(ResponseCodesEnum.DEPARTMENT_ALREADY_EXISTS.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DEPARTMENT_ALREADY_EXISTS.getDescription())
                    .build();
        }
        Optional<DepartmentDTO> savedDepartment = departmentDAO.createDepartment(DepartmentDTO.builder()
                .departmentName(requestBean.getDepartmentName())
                .code(requestBean.getCode())
                .description(requestBean.getDescription())
                .location(requestBean.getLocation())
                .director(requestBean.getDirector())
                .phone(requestBean.getPhone())
                .email(requestBean.getEmail())
                .coordinator(requestBean.getCoordinator())
                .build());
        if (savedDepartment.isEmpty()) {
            return RegisterDepartmentResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return RegisterDepartmentResponseBean.builder()
                .departmentCode(savedDepartment.get().getCode())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RegisterRoomResponseBean registerRoom(RegisterRoomRequestBean requestBean) {
        if (departmentDAO.retrieveByCode(requestBean.getDepartmentCode()).isEmpty()) {
            return RegisterRoomResponseBean.builder()
                    .status(ResponseCodesEnum.DEPARTMENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.DEPARTMENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DEPARTMENT_NOT_FOUND.getDescription())
                    .build();
        }
        if (departmentRoomDAO.retrieveRoomByDepartmentAndNumber(requestBean.getDepartmentCode(),
                requestBean.getRoomNumber()).isPresent()) {
            return RegisterRoomResponseBean.builder()
                    .status(ResponseCodesEnum.ROOM_ALREADY_EXISTS.getStatus())
                    .responseCode(ResponseCodesEnum.ROOM_ALREADY_EXISTS.getErrorCode())
                    .responseMessage(ResponseCodesEnum.ROOM_ALREADY_EXISTS.getDescription())
                    .build();
        }
        Optional<RoomDTO> savedRoom = departmentRoomDAO.createRoom(RoomDTO.builder()
                .departmentCode(requestBean.getDepartmentCode())
                .roomNumber(requestBean.getRoomNumber())
                .bedCount(requestBean.getBedCount())
                .build());
        if (savedRoom.isEmpty()) {
            return RegisterRoomResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return RegisterRoomResponseBean.builder()
                .roomNumber(savedRoom.get().getRoomNumber())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RegisterBedRoomResponseBean registerBedRoom(RegisterBedRoomRequestBean requestBean) {
        if (departmentDAO.retrieveByCode(requestBean.getDepartmentCode()).isEmpty()) {
            return RegisterBedRoomResponseBean.builder()
                    .status(ResponseCodesEnum.DEPARTMENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.DEPARTMENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DEPARTMENT_NOT_FOUND.getDescription())
                    .build();
        }
        if (departmentRoomDAO.retrieveRoomByDepartmentAndNumber(requestBean.getDepartmentCode(),
                requestBean.getRoomNumber()).isEmpty()) {
            return RegisterBedRoomResponseBean.builder()
                    .status(ResponseCodesEnum.ROOM_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.ROOM_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.ROOM_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<BedDTO> savedBed = departmentRoomBedDAO.createBed(BedDTO.builder()
                .departmentCode(requestBean.getDepartmentCode())
                .roomNumber(requestBean.getRoomNumber())
                .bedNumber(requestBean.getBedNumber())
                .isMotorized(requestBean.getIsBedMotorized())
                .bedSerialNumber(requestBean.getBedSerialNumber())
                .build());
        if (savedBed.isEmpty()) {
            return RegisterBedRoomResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return RegisterBedRoomResponseBean.builder()
                .bedNumber(savedBed.get().getBedNumber())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public AssignRoomBedResponseBean assignBedToPatient(AssignRoomBedRequestBean requestBean) {
        if (patientDAO.findPatientByFiscalCode(requestBean.getPatientFiscalCode()).isEmpty()) {
            return AssignRoomBedResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription())
                    .build();
        }
        if (departmentDAO.retrieveByCode(requestBean.getDepartmentCode()).isEmpty()) {
            return AssignRoomBedResponseBean.builder()
                    .status(ResponseCodesEnum.DEPARTMENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.DEPARTMENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DEPARTMENT_NOT_FOUND.getDescription())
                    .build();
        }
        if (departmentRoomDAO.retrieveRoomByDepartmentAndNumber(requestBean.getDepartmentCode(),
                requestBean.getRoomNumber()).isEmpty()) {
            return AssignRoomBedResponseBean.builder()
                    .status(ResponseCodesEnum.ROOM_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.ROOM_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.ROOM_NOT_FOUND.getDescription())
                    .build();
        }
        if (departmentRoomBedDAO.retrieveBedByBedNumberAndRoomNumberAndDepartmentCode(requestBean.getBedNumber(),
                requestBean.getRoomNumber(),
                requestBean.getDepartmentCode()).isEmpty()) {
            return AssignRoomBedResponseBean.builder()
                    .status(ResponseCodesEnum.BED_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.BED_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.BED_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<Integer> lastAssociation = departmentRoomBedDAO.retrieveBedLastAssociationState(requestBean.getBedNumber(),
                requestBean.getRoomNumber(),
                requestBean.getDepartmentCode());
        if ((lastAssociation.isPresent() &&
                BedOperationTypeEnum.getOperationByCode(lastAssociation.get()).equals(BedOperationTypeEnum.ASSIGNED))) {
            return AssignRoomBedResponseBean.builder()
                    .status(ResponseCodesEnum.BED_ALREADY_ASSIGNED.getStatus())
                    .responseCode(ResponseCodesEnum.BED_ALREADY_ASSIGNED.getErrorCode())
                    .responseMessage(ResponseCodesEnum.BED_ALREADY_ASSIGNED.getDescription())
                    .build();
        }
        Optional<BedDTO> savedState = departmentRoomBedDAO.assignBedToPatient(requestBean.getBedNumber(),
                requestBean.getRoomNumber(),
                requestBean.getDepartmentCode(),
                requestBean.getPatientFiscalCode());
        if (savedState.isEmpty()) {
            return AssignRoomBedResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return AssignRoomBedResponseBean.builder()
                .bedNumber(savedState.get().getBedNumber())
                .roomNumber(savedState.get().getRoomNumber())
                .departmentCode(savedState.get().getDepartmentCode())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public UnassignRoomBedResponseBean unassignBedToPatient(UnassignRoomBedRequestBean requestBean) {
        if (patientDAO.findPatientByFiscalCode(requestBean.getPatientFiscalCode()).isEmpty()) {
            return UnassignRoomBedResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription())
                    .build();
        }
        if (departmentDAO.retrieveByCode(requestBean.getDepartmentCode()).isEmpty()) {
            return UnassignRoomBedResponseBean.builder()
                    .status(ResponseCodesEnum.DEPARTMENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.DEPARTMENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DEPARTMENT_NOT_FOUND.getDescription())
                    .build();
        }
        if (departmentRoomDAO.retrieveRoomByDepartmentAndNumber(requestBean.getDepartmentCode(),
                requestBean.getRoomNumber()).isEmpty()) {
            return UnassignRoomBedResponseBean.builder()
                    .status(ResponseCodesEnum.ROOM_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.ROOM_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.ROOM_NOT_FOUND.getDescription())
                    .build();
        }
        if (departmentRoomBedDAO.retrieveBedByBedNumberAndRoomNumberAndDepartmentCode(requestBean.getBedNumber(),
                requestBean.getRoomNumber(),
                requestBean.getDepartmentCode()).isEmpty()) {
            return UnassignRoomBedResponseBean.builder()
                    .status(ResponseCodesEnum.BED_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.BED_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.BED_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<Integer> lastAssociation = departmentRoomBedDAO.retrieveBedLastAssociationState(requestBean.getBedNumber(),
                requestBean.getRoomNumber(),
                requestBean.getDepartmentCode());
        if ((lastAssociation.isEmpty() ||
                BedOperationTypeEnum.getOperationByCode(lastAssociation.get()).equals(BedOperationTypeEnum.RETURNED))) {
            return UnassignRoomBedResponseBean.builder()
                    .status(ResponseCodesEnum.BED_NOT_ASSIGNED.getStatus())
                    .responseCode(ResponseCodesEnum.BED_NOT_ASSIGNED.getErrorCode())
                    .responseMessage(ResponseCodesEnum.BED_NOT_ASSIGNED.getDescription())
                    .build();
        }
        if (Boolean.FALSE.equals(departmentRoomBedDAO.isBedAssignedToPatient(requestBean.getBedNumber(),
                requestBean.getRoomNumber(),
                requestBean.getDepartmentCode(),
                requestBean.getPatientFiscalCode()))) {
            return UnassignRoomBedResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_ASSIGNED_TO_GIVEN_BED.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_ASSIGNED_TO_GIVEN_BED.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_ASSIGNED_TO_GIVEN_BED.getDescription())
                    .build();
        }
        Optional<BedDTO> savedState = departmentRoomBedDAO.unassignBedToPatient(requestBean.getBedNumber(),
                requestBean.getRoomNumber(),
                requestBean.getDepartmentCode(),
                requestBean.getPatientFiscalCode());
        if (savedState.isEmpty()) {
            return UnassignRoomBedResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return UnassignRoomBedResponseBean.builder()
                .bedNumber(savedState.get().getBedNumber())
                .roomNumber(savedState.get().getRoomNumber())
                .departmentCode(requestBean.getDepartmentCode())
                .build();
    }
}
