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
import com.newhorizon.nhmonolithicapplication.dto.PatientDTO;
import com.newhorizon.nhmonolithicapplication.enums.DeviceOperationTypeEnum;
import com.newhorizon.nhmonolithicapplication.enums.DeviceTypeEnum;
import com.newhorizon.nhmonolithicapplication.enums.GenderTypeEnum;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
@Transactional
class DeviceServiceMockTest {
    @Mock
    DeviceDAO deviceDAO;
    @Mock
    PatientDAO patientDAO;
    @InjectMocks
    DeviceServiceImpl deviceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createDeviceEmptySaving() {
        when(deviceDAO.findBySerialNumber(any(String.class)))
                .thenReturn(Optional.empty());
        when(deviceDAO.registerDevice(any(DeviceDTO.class)))
                .thenReturn(Optional.empty());
        RegisterDeviceResponseBean serviceResponse = deviceService.createDevice(RegisterDeviceRequestBean.builder()
                .deviceSerialNumber(UUID.randomUUID().toString())
                .deviceTlsCertificate("null")
                .deviceType(DeviceTypeEnum.MULTI_PARAMETER_MONITOR.getDeviceTypeCode())
                .build());

        assertEquals(ResponseCodesEnum.GENERIC_ERROR.getStatus(), serviceResponse.getStatus());
        assertEquals(ResponseCodesEnum.GENERIC_ERROR.getErrorCode(), serviceResponse.getResponseCode());
        assertEquals(ResponseCodesEnum.GENERIC_ERROR.getDescription(), serviceResponse.getResponseMessage());
    }

    @Test
    void assignDeviceToPatientKOEmptyAssignment() {
        UUID deviceId = UUID.randomUUID();
        String patientCF = "RSSNTN00R27L049N";
        AssignDeviceRequestBean requestBean = AssignDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .patientFiscalCode(patientCF)
                .build();
        when(patientDAO.findPatientByFiscalCode(patientCF))
                .thenReturn(Optional.of(PatientDTO.builder()
                        .patientName("Antonio")
                        .patientSurname("Russi")
                        .dateOfBirth(LocalDate.of(2000, 10, 27))
                        .patientFiscalCode(patientCF)
                        .patientGender(GenderTypeEnum.MALE.getGenderCode())
                        .build()));
        when(deviceDAO.findBySerialNumber(deviceId.toString()))
                .thenReturn(Optional.of(DeviceDTO.builder()
                        .deviceSerialNumber(deviceId.toString())
                        .deviceTlsCertificate("null")
                        .deviceType(DeviceTypeEnum.MULTI_PARAMETER_MONITOR.getDeviceTypeCode())
                        .isEnabled(Boolean.TRUE)
                        .build()));
        when(deviceDAO.retrieveDeviceLastAssociationState(any(String.class)))
                .thenReturn(Optional.empty());
        when(deviceDAO.assignDeviceToPatient(any(String.class), eq(patientCF)))
                .thenReturn(Optional.empty());

        AssignDeviceResponseBean serviceResponse = deviceService.assignDeviceToPatient(requestBean);
        assertEquals(ResponseCodesEnum.GENERIC_ERROR.getStatus(), serviceResponse.getStatus());
        assertEquals(ResponseCodesEnum.GENERIC_ERROR.getErrorCode(), serviceResponse.getResponseCode());
        assertEquals(ResponseCodesEnum.GENERIC_ERROR.getDescription(), serviceResponse.getResponseMessage());
    }

    @Test
    void unassingDeviceKOEmptyUnassignment() {
        UUID deviceId = UUID.randomUUID();
        String patientCF = "RSSNTN00R27L049N";
        UnassignDeviceRequestBean requestBean = UnassignDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .userId(patientCF)
                .build();
        when(patientDAO.findPatientByFiscalCode(patientCF))
                .thenReturn(Optional.of(PatientDTO.builder()
                        .patientName("Antonio")
                        .patientSurname("Russi")
                        .dateOfBirth(LocalDate.of(2000, 10, 27))
                        .patientFiscalCode(patientCF)
                        .patientGender(GenderTypeEnum.MALE.getGenderCode())
                        .build()));
        when(deviceDAO.findBySerialNumber(deviceId.toString()))
                .thenReturn(Optional.of(DeviceDTO.builder()
                        .deviceSerialNumber(deviceId.toString())
                        .deviceTlsCertificate("null")
                        .deviceType(DeviceTypeEnum.MULTI_PARAMETER_MONITOR.getDeviceTypeCode())
                        .isEnabled(Boolean.TRUE)
                        .build()));
        when(deviceDAO.retrieveDeviceLastAssociationState(any(String.class)))
                .thenReturn(Optional.of(DeviceOperationTypeEnum.ASSIGNED.getDeviceOperationTypeCode()));
        when(deviceDAO.isDeviceAssignedToPatient(any(String.class), eq(patientCF)))
                .thenReturn(Boolean.TRUE);
        when(deviceDAO.unassignDeviceToPatient(any(String.class), eq(patientCF)))
                .thenReturn(Optional.empty());

        UnassignDeviceResponseBean serviceResponse = deviceService.unassignDeviceToPatient(requestBean);
        assertEquals(ResponseCodesEnum.GENERIC_ERROR.getStatus(), serviceResponse.getStatus());
        assertEquals(ResponseCodesEnum.GENERIC_ERROR.getErrorCode(), serviceResponse.getResponseCode());
        assertEquals(ResponseCodesEnum.GENERIC_ERROR.getDescription(), serviceResponse.getResponseMessage());
    }

    @Test
    void toggleEnablementKOEmptySaving() {
        UUID deviceId = UUID.randomUUID();
        ToggleDeviceEnablementRequestBean requestBean = ToggleDeviceEnablementRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .isEnabled(Boolean.FALSE)
                .build();
        when(deviceDAO.findBySerialNumber(deviceId.toString()))
                .thenReturn(Optional.of(DeviceDTO.builder()
                        .deviceSerialNumber(deviceId.toString())
                        .deviceTlsCertificate("null")
                        .deviceType(DeviceTypeEnum.MULTI_PARAMETER_MONITOR.getDeviceTypeCode())
                        .isEnabled(Boolean.TRUE)
                        .build()));
        when(deviceDAO.updateDeviceEnablement(eq(deviceId.toString()), any(Boolean.class)))
                .thenReturn(Optional.empty());

        ToggleDeviceEnablementResponseBean serviceResponse = deviceService.toggleDeviceEnablement(requestBean);
        assertEquals(ResponseCodesEnum.GENERIC_ERROR.getStatus(), serviceResponse.getStatus());
        assertEquals(ResponseCodesEnum.GENERIC_ERROR.getErrorCode(), serviceResponse.getResponseCode());
        assertEquals(ResponseCodesEnum.GENERIC_ERROR.getDescription(), serviceResponse.getResponseMessage());
    }
}
