package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.AssignDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.ToggleDeviceEnablementRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnassignDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.AssignDeviceResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterDeviceResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.ToggleDeviceEnablementResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UnassignDeviceResponseBean;
import com.newhorizon.nhmonolithicapplication.data.DeviceDAO;
import com.newhorizon.nhmonolithicapplication.data.PatientDAO;
import com.newhorizon.nhmonolithicapplication.dto.DeviceDTO;
import com.newhorizon.nhmonolithicapplication.enums.DeviceOperationTypeEnum;
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
public class DeviceServiceImpl implements DeviceService {
    private final DeviceDAO deviceDAO;
    private final PatientDAO patientDAO;

    @Override
    @SuppressWarnings("unchecked")
    public RegisterDeviceResponseBean createDevice(RegisterDeviceRequestBean requestBean) {
        if (deviceDAO.findBySerialNumber(requestBean.getDeviceSerialNumber()).isPresent()) {
            return RegisterDeviceResponseBean.builder()
                    .status(ResponseCodesEnum.DEVICE_ALREADY_EXISTS.getStatus())
                    .responseCode(ResponseCodesEnum.DEVICE_ALREADY_EXISTS.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DEVICE_ALREADY_EXISTS.getDescription())
                    .build();
        }
        Optional<DeviceDTO> savedDevice = deviceDAO.registerDevice(DeviceDTO.builder()
                .deviceSerialNumber(requestBean.getDeviceSerialNumber())
                .deviceTlsCertificate(requestBean.getDeviceTlsCertificate())
                .deviceType(requestBean.getDeviceType())
                .isEnabled(Boolean.FALSE)
                .build());
        if (savedDevice.isEmpty()) {
            return RegisterDeviceResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return RegisterDeviceResponseBean
                .builder()
                .serialNumber(savedDevice.get().getDeviceSerialNumber())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public AssignDeviceResponseBean assignDeviceToPatient(AssignDeviceRequestBean requestBean) {
        if (patientDAO.findPatientByFiscalCode(requestBean.getPatientFiscalCode()).isEmpty()) {
            return AssignDeviceResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<DeviceDTO> deviceDTO = deviceDAO.findBySerialNumber(requestBean.getDeviceSerialNumber());
        if (deviceDTO.isEmpty()) {
            return AssignDeviceResponseBean.builder()
                    .status(ResponseCodesEnum.DEVICE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.DEVICE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DEVICE_NOT_FOUND.getDescription())
                    .build();
        }
        if (Boolean.FALSE.equals(deviceDTO.get().getIsEnabled())) {
            return AssignDeviceResponseBean.builder()
                    .status(ResponseCodesEnum.DEVICE_NOT_ENABLED.getStatus())
                    .responseCode(ResponseCodesEnum.DEVICE_NOT_ENABLED.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DEVICE_NOT_ENABLED.getDescription())
                    .build();
        }
        Optional<Integer> lastAssociation = deviceDAO.retrieveDeviceLastAssociationState(requestBean.getDeviceSerialNumber());
        if (lastAssociation.isPresent() &&
                DeviceOperationTypeEnum.getOperationByCode(lastAssociation.get()).equals(DeviceOperationTypeEnum.ASSIGNED)) {
            return AssignDeviceResponseBean.builder()
                    .status(ResponseCodesEnum.DEVICE_ALREADY_ASSIGNED.getStatus())
                    .responseCode(ResponseCodesEnum.DEVICE_ALREADY_ASSIGNED.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DEVICE_ALREADY_ASSIGNED.getDescription())
                    .build();
        }
        Optional<DeviceDTO> savedState = deviceDAO.assignDeviceToPatient(requestBean.getDeviceSerialNumber(), requestBean.getPatientFiscalCode());
        if (savedState.isEmpty()) {
            return AssignDeviceResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return AssignDeviceResponseBean.builder()
                .deviceId(requestBean.getDeviceSerialNumber())
                .userId(requestBean.getPatientFiscalCode())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public UnassignDeviceResponseBean unassignDeviceToPatient(UnassignDeviceRequestBean requestBean) {
        if (patientDAO.findPatientByFiscalCode(requestBean.getUserId()).isEmpty()) {
            return UnassignDeviceResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<DeviceDTO> deviceDTO = deviceDAO.findBySerialNumber(requestBean.getDeviceSerialNumber());
        if (deviceDTO.isEmpty()) {
            return UnassignDeviceResponseBean.builder()
                    .status(ResponseCodesEnum.DEVICE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.DEVICE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DEVICE_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<Integer> lastAssociation = deviceDAO.retrieveDeviceLastAssociationState(requestBean.getDeviceSerialNumber());
        if (lastAssociation.isEmpty() ||
                DeviceOperationTypeEnum.getOperationByCode(lastAssociation.get()).equals(DeviceOperationTypeEnum.RETURNED)) {
            return UnassignDeviceResponseBean.builder()
                    .status(ResponseCodesEnum.DEVICE_NOT_ASSIGNED.getStatus())
                    .responseCode(ResponseCodesEnum.DEVICE_NOT_ASSIGNED.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DEVICE_NOT_ASSIGNED.getDescription())
                    .build();
        }
        if (Boolean.FALSE.equals(deviceDAO.isDeviceAssignedToPatient(requestBean.getDeviceSerialNumber(), requestBean.getUserId()))) {
            return UnassignDeviceResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_ASSIGNED_TO_GIVEN_DEVICE.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_ASSIGNED_TO_GIVEN_DEVICE.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_ASSIGNED_TO_GIVEN_DEVICE.getDescription())
                    .build();
        }
        Optional<DeviceDTO> savedState = deviceDAO.unassignDeviceToPatient(requestBean.getDeviceSerialNumber(), requestBean.getUserId());
        if (savedState.isEmpty()) {
            return UnassignDeviceResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return UnassignDeviceResponseBean.builder()
                .deviceId(requestBean.getDeviceSerialNumber())
                .userId(requestBean.getUserId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public ToggleDeviceEnablementResponseBean toggleDeviceEnablement(ToggleDeviceEnablementRequestBean requestBean) {
        if (deviceDAO.findBySerialNumber(requestBean.getDeviceSerialNumber()).isEmpty()) {
            return ToggleDeviceEnablementResponseBean.builder()
                    .status(ResponseCodesEnum.DEVICE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.DEVICE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DEVICE_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<DeviceDTO> updatedDevice = deviceDAO.updateDeviceEnablement(requestBean.getDeviceSerialNumber(),
                requestBean.getIsEnabled());
        if (updatedDevice.isEmpty()) {
            return ToggleDeviceEnablementResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return ToggleDeviceEnablementResponseBean.builder()
                .deviceSerialNumber(updatedDevice.get().getDeviceSerialNumber())
                .build();
    }
}
