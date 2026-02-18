package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.NhMonolithicApplicationTest;
import com.newhorizon.nhmonolithicapplication.beans.request.AssignDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterPatientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.ToggleDeviceEnablementRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnassignDeviceRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.AssignDeviceResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterDeviceResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.ToggleDeviceEnablementResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UnassignDeviceResponseBean;
import com.newhorizon.nhmonolithicapplication.enums.DeviceTypeEnum;
import com.newhorizon.nhmonolithicapplication.enums.GenderTypeEnum;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.UUID;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
@Transactional
class DeviceServiceTest extends NhMonolithicApplicationTest {
    @Autowired
    DeviceServiceImpl deviceService;
    @Autowired
    PatientServiceImpl patientService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Create device successfully")
    void createDeviceOk() {
        RegisterDeviceRequestBean requestBean = Instancio.of(RegisterDeviceRequestBean.class)
                .set(field(RegisterDeviceRequestBean::getDeviceType), DeviceTypeEnum.MULTI_PARAMETER_MONITOR.getDeviceTypeCode())
                .create();
        RegisterDeviceResponseBean serviceResponse = deviceService.createDevice(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
        assertEquals(requestBean.getDeviceSerialNumber(), serviceResponse.getSerialNumber());
    }

    @Test
    @DisplayName("Create device failing with device already exists")
    void createDeviceKOAlreadyExists() {
        RegisterDeviceRequestBean requestBean = Instancio.of(RegisterDeviceRequestBean.class)
                .set(field(RegisterDeviceRequestBean::getDeviceType), DeviceTypeEnum.MULTI_PARAMETER_MONITOR.getDeviceTypeCode())
                .create();
        RegisterDeviceResponseBean serviceResponse = deviceService.createDevice(requestBean);
        log.info("Create device,{}", serviceResponse);
        serviceResponse = deviceService.createDevice(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.DEVICE_ALREADY_EXISTS, serviceResponse);
    }

    @Test
    @DisplayName("Toggle device enablement with device not found")
    void toggleDeviceEnablementKODeviceNotFound() {
        RegisterDeviceRequestBean requestBeanDevice = Instancio.of(RegisterDeviceRequestBean.class)
                .set(field(RegisterDeviceRequestBean::getDeviceType), DeviceTypeEnum.MULTI_PARAMETER_MONITOR.getDeviceTypeCode())
                .create();
        ToggleDeviceEnablementRequestBean requestBean=Instancio.of(ToggleDeviceEnablementRequestBean.class)
                .set(field(ToggleDeviceEnablementRequestBean::getDeviceSerialNumber),requestBeanDevice.getDeviceSerialNumber())
                .create();
        ToggleDeviceEnablementResponseBean serviceResponse = deviceService.toggleDeviceEnablement(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.DEVICE_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Toggle device enablement successfully")
    void toggleDeviceEnablementOK() {
        RegisterDeviceRequestBean requestBeanDevice = Instancio.of(RegisterDeviceRequestBean.class)
                .set(field(RegisterDeviceRequestBean::getDeviceType), DeviceTypeEnum.MULTI_PARAMETER_MONITOR.getDeviceTypeCode())
                .create();
        ToggleDeviceEnablementRequestBean requestBean=Instancio.of(ToggleDeviceEnablementRequestBean.class)
                .set(field(ToggleDeviceEnablementRequestBean::getDeviceSerialNumber),requestBeanDevice.getDeviceSerialNumber())
                .create();
        deviceService.createDevice(requestBeanDevice);
        ToggleDeviceEnablementResponseBean serviceResponse = deviceService.toggleDeviceEnablement(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
        assertEquals(requestBeanDevice.getDeviceSerialNumber(), serviceResponse.getDeviceSerialNumber());
    }

    @Test
    void assignDeviceToPatientKOPatientNotFound() {
        //TODO refactor with instancio
        String patientCF = "RSSNTN00R27L049N";
        UUID deviceId = UUID.randomUUID();
        AssignDeviceResponseBean serviceResponse = deviceService.assignDeviceToPatient(AssignDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .patientFiscalCode(patientCF)
                .build());

        assertEquals(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode(), serviceResponse.getResponseCode());
        assertEquals(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription(), serviceResponse.getResponseMessage());
        assertEquals(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus(), serviceResponse.getStatus());
    }

    @Test
    void assignDeviceToPatientKODeviceNotFound() {
        //TODO refactor with instancio
        String patientCF = "RSSNTN00R27L049N";
        UUID deviceId = UUID.randomUUID();
        patientService.registerNewPatient(RegisterPatientRequestBean.builder()
                .patientName("Antonio")
                .patientSurname("Russi")
                .dateOfBirth(LocalDate.of(2000, 10, 27))
                .patientFiscalCode(patientCF)
                .patientGender(GenderTypeEnum.MALE.getGenderCode())
                .build());
        AssignDeviceResponseBean serviceResponse = deviceService.assignDeviceToPatient(AssignDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .patientFiscalCode(patientCF)
                .build());

        assertEquals(ResponseCodesEnum.DEVICE_NOT_FOUND.getErrorCode(), serviceResponse.getResponseCode());
        assertEquals(ResponseCodesEnum.DEVICE_NOT_FOUND.getDescription(), serviceResponse.getResponseMessage());
        assertEquals(ResponseCodesEnum.DEVICE_NOT_FOUND.getStatus(), serviceResponse.getStatus());
    }

    @Test
    void assignDeviceToPatientKODeviceDisabled() {
        //TODO refactor with instancio
        String patientCF = "RSSNTN00R27L049N";
        UUID deviceId = UUID.randomUUID();
        patientService.registerNewPatient(RegisterPatientRequestBean.builder()
                .patientName("Antonio")
                .patientSurname("Russi")
                .dateOfBirth(LocalDate.of(2000, 10, 27))
                .patientFiscalCode(patientCF)
                .patientGender(GenderTypeEnum.MALE.getGenderCode())
                .build());
        deviceService.createDevice(RegisterDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .deviceTlsCertificate("null")
                .deviceType(DeviceTypeEnum.MULTI_PARAMETER_MONITOR.getDeviceTypeCode())
                .build());
        AssignDeviceResponseBean serviceResponse = deviceService.assignDeviceToPatient(AssignDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .patientFiscalCode(patientCF)
                .build());

        assertEquals(ResponseCodesEnum.DEVICE_NOT_ENABLED.getErrorCode(), serviceResponse.getResponseCode());
        assertEquals(ResponseCodesEnum.DEVICE_NOT_ENABLED.getDescription(), serviceResponse.getResponseMessage());
        assertEquals(ResponseCodesEnum.DEVICE_NOT_ENABLED.getStatus(), serviceResponse.getStatus());
    }

    @Test
    void assignDeviceToPatientKODeviceAlreadyAssigned() {
        //TODO refactor with instancio
        String patientCF = "RSSNTN00R27L049N";
        UUID deviceId = UUID.randomUUID();
        patientService.registerNewPatient(RegisterPatientRequestBean.builder()
                .patientName("Antonio")
                .patientSurname("Russi")
                .dateOfBirth(LocalDate.of(2000, 10, 27))
                .patientFiscalCode(patientCF)
                .patientGender(GenderTypeEnum.MALE.getGenderCode())
                .build());
        deviceService.createDevice(RegisterDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .deviceTlsCertificate("null")
                .deviceType(DeviceTypeEnum.MULTI_PARAMETER_MONITOR.getDeviceTypeCode())
                .build());
        deviceService.toggleDeviceEnablement(ToggleDeviceEnablementRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .isEnabled(Boolean.TRUE)
                .build());
        AssignDeviceResponseBean serviceResponse = deviceService.assignDeviceToPatient(AssignDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .patientFiscalCode(patientCF)
                .build());
        log.info("Testing assignment for {}", serviceResponse.getUserId());
        serviceResponse = deviceService.assignDeviceToPatient(AssignDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .patientFiscalCode(patientCF)
                .build());

        assertEquals(ResponseCodesEnum.DEVICE_ALREADY_ASSIGNED.getErrorCode(), serviceResponse.getResponseCode());
        assertEquals(ResponseCodesEnum.DEVICE_ALREADY_ASSIGNED.getDescription(), serviceResponse.getResponseMessage());
        assertEquals(ResponseCodesEnum.DEVICE_ALREADY_ASSIGNED.getStatus(), serviceResponse.getStatus());
    }

    @Test
    void assignDeviceToPatientOK() {
        //TODO refactor with instancio

        String patientCF = "RSSNTN00R27L049N";
        UUID deviceId = UUID.randomUUID();
        patientService.registerNewPatient(RegisterPatientRequestBean.builder()
                .patientName("Antonio")
                .patientSurname("Russi")
                .dateOfBirth(LocalDate.of(2000, 10, 27))
                .patientFiscalCode(patientCF)
                .patientGender(GenderTypeEnum.MALE.getGenderCode())
                .build());
        deviceService.createDevice(RegisterDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .deviceTlsCertificate("null")
                .deviceType(DeviceTypeEnum.MULTI_PARAMETER_MONITOR.getDeviceTypeCode())
                .build());
        deviceService.toggleDeviceEnablement(ToggleDeviceEnablementRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .isEnabled(Boolean.TRUE)
                .build());
        AssignDeviceResponseBean serviceResponse = deviceService.assignDeviceToPatient(AssignDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .patientFiscalCode(patientCF)
                .build());

        assertEquals(ResponseCodesEnum.OK.getErrorCode(), serviceResponse.getResponseCode());
        assertEquals(ResponseCodesEnum.OK.getDescription(), serviceResponse.getResponseMessage());
        assertEquals(ResponseCodesEnum.OK.getStatus(), serviceResponse.getStatus());
        assertEquals(deviceId.toString(), serviceResponse.getDeviceId());
        assertEquals(patientCF, serviceResponse.getUserId());
    }

    @Test
    void unassignDeviceToPatientKOPatientNotFound() {
        //TODO refactor with instancio
        UUID deviceId = UUID.randomUUID();
        String patientCF = "RSSNTN00R27L049N";
        UnassignDeviceResponseBean serviceResponse = deviceService.unassignDeviceToPatient(UnassignDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .userId(patientCF)
                .build());

        assertEquals(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode(), serviceResponse.getResponseCode());
        assertEquals(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription(), serviceResponse.getResponseMessage());
        assertEquals(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus(), serviceResponse.getStatus());
    }

    @Test
    void unassignDeviceToPatientKODeviceNotFound() {
        //TODO refactor with instancio
        UUID deviceId = UUID.randomUUID();
        String patientCF = "RSSNTN00R27L049N";
        patientService.registerNewPatient(RegisterPatientRequestBean.builder()
                .patientName("Antonio")
                .patientSurname("Russi")
                .dateOfBirth(LocalDate.of(2000, 10, 27))
                .patientFiscalCode(patientCF)
                .patientGender(GenderTypeEnum.MALE.getGenderCode())
                .build());
        UnassignDeviceResponseBean serviceResponse = deviceService.unassignDeviceToPatient(UnassignDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .userId(patientCF)
                .build());

        assertEquals(ResponseCodesEnum.DEVICE_NOT_FOUND.getErrorCode(), serviceResponse.getResponseCode());
        assertEquals(ResponseCodesEnum.DEVICE_NOT_FOUND.getDescription(), serviceResponse.getResponseMessage());
        assertEquals(ResponseCodesEnum.DEVICE_NOT_FOUND.getStatus(), serviceResponse.getStatus());
    }

    @Test
    void unassignDeviceToPatientKODeviceNotAssigned() {
        //TODO refactor with instancio
        UUID deviceId = UUID.randomUUID();
        String patientCF = "RSSNTN00R27L049N";
        patientService.registerNewPatient(RegisterPatientRequestBean.builder()
                .patientName("Antonio")
                .patientSurname("Russi")
                .dateOfBirth(LocalDate.of(2000, 10, 27))
                .patientFiscalCode(patientCF)
                .patientGender(GenderTypeEnum.MALE.getGenderCode())
                .build());
        deviceService.createDevice(RegisterDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .deviceTlsCertificate("null")
                .deviceType(DeviceTypeEnum.MULTI_PARAMETER_MONITOR.getDeviceTypeCode())
                .build());
        deviceService.toggleDeviceEnablement(ToggleDeviceEnablementRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .isEnabled(Boolean.TRUE)
                .build());
        UnassignDeviceResponseBean serviceResponse = deviceService.unassignDeviceToPatient(UnassignDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .userId(patientCF)
                .build());

        assertEquals(ResponseCodesEnum.DEVICE_NOT_ASSIGNED.getErrorCode(), serviceResponse.getResponseCode());
        assertEquals(ResponseCodesEnum.DEVICE_NOT_ASSIGNED.getDescription(), serviceResponse.getResponseMessage());
        assertEquals(ResponseCodesEnum.DEVICE_NOT_ASSIGNED.getStatus(), serviceResponse.getStatus());
    }

    @Test
    void unassignDeviceToPatientKODeviceNotAssignedToGivenPatient() {
        //TODO refactor with instancio
        UUID deviceId = UUID.randomUUID();
        String patientCF = "RSSNTN00R27L049N";
        String patientCFSecond = "TDSLRF02M42L049Q";
        patientService.registerNewPatient(RegisterPatientRequestBean.builder()
                .patientName("Antonio")
                .patientSurname("Russi")
                .dateOfBirth(LocalDate.of(2000, 10, 27))
                .patientFiscalCode(patientCF)
                .patientGender(GenderTypeEnum.MALE.getGenderCode())
                .build());
        patientService.registerNewPatient(RegisterPatientRequestBean.builder()
                .patientName("Elenoire Francesca")
                .patientSurname("Tedesco")
                .dateOfBirth(LocalDate.of(2002, 8, 2))
                .patientFiscalCode(patientCFSecond)
                .patientGender(GenderTypeEnum.FEMALE.getGenderCode())
                .build());
        deviceService.createDevice(RegisterDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .deviceTlsCertificate("null")
                .deviceType(DeviceTypeEnum.MULTI_PARAMETER_MONITOR.getDeviceTypeCode())
                .build());
        deviceService.toggleDeviceEnablement(ToggleDeviceEnablementRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .isEnabled(Boolean.TRUE)
                .build());
        deviceService.assignDeviceToPatient(AssignDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .patientFiscalCode(patientCF)
                .build());
        UnassignDeviceResponseBean serviceResponse = deviceService.unassignDeviceToPatient(UnassignDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .userId(patientCFSecond)
                .build());

        assertEquals(ResponseCodesEnum.PATIENT_NOT_ASSIGNED_TO_GIVEN_DEVICE.getErrorCode(), serviceResponse.getResponseCode());
        assertEquals(ResponseCodesEnum.PATIENT_NOT_ASSIGNED_TO_GIVEN_DEVICE.getDescription(), serviceResponse.getResponseMessage());
        assertEquals(ResponseCodesEnum.PATIENT_NOT_ASSIGNED_TO_GIVEN_DEVICE.getStatus(), serviceResponse.getStatus());
    }

    @Test
    void unassignDeviceToPatientOK() {
        //TODO refactor with instancio
        UUID deviceId = UUID.randomUUID();
        String patientCF = "RSSNTN00R27L049N";
        patientService.registerNewPatient(RegisterPatientRequestBean.builder()
                .patientName("Antonio")
                .patientSurname("Russi")
                .dateOfBirth(LocalDate.of(2000, 10, 27))
                .patientFiscalCode(patientCF)
                .patientGender(GenderTypeEnum.MALE.getGenderCode())
                .build());
        deviceService.createDevice(RegisterDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .deviceTlsCertificate("null")
                .deviceType(DeviceTypeEnum.MULTI_PARAMETER_MONITOR.getDeviceTypeCode())
                .build());
        deviceService.toggleDeviceEnablement(ToggleDeviceEnablementRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .isEnabled(Boolean.TRUE)
                .build());
        deviceService.assignDeviceToPatient(AssignDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .patientFiscalCode(patientCF)
                .build());
        UnassignDeviceResponseBean serviceResponse = deviceService.unassignDeviceToPatient(UnassignDeviceRequestBean.builder()
                .deviceSerialNumber(deviceId.toString())
                .userId(patientCF)
                .build());

        assertEquals(ResponseCodesEnum.OK.getErrorCode(), serviceResponse.getResponseCode());
        assertEquals(ResponseCodesEnum.OK.getDescription(), serviceResponse.getResponseMessage());
        assertEquals(ResponseCodesEnum.OK.getStatus(), serviceResponse.getStatus());
        assertEquals(patientCF, serviceResponse.getUserId());
        assertEquals(deviceId.toString(), serviceResponse.getDeviceId());
    }

}