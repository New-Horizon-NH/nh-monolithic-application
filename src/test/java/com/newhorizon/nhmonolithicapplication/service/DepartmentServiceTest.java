package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.NhMonolithicApplicationTest;
import com.newhorizon.nhmonolithicapplication.beans.request.AssignRoomBedRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterBedRoomRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterDepartmentRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterPatientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterRoomRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnassignRoomBedRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.AssignRoomBedResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterBedRoomResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterDepartmentResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterRoomResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UnassignRoomBedResponseBean;
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

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Transactional
class DepartmentServiceTest extends NhMonolithicApplicationTest {
    @Autowired
    DepartmentServiceImpl departmentService;
    @Autowired
    PatientServiceImpl patientService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Register department successfully")
    void registerDepartmentOK() {
        RegisterDepartmentRequestBean requestBean = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterDepartmentResponseBean serviceResponse = departmentService.registerDepartement(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
        assertEquals(requestBean.getCode(), serviceResponse.getDepartmentCode());
    }

    @Test
    @DisplayName("Register department failing with already used department code")
    void registerDepartmentKOAlreadyExists() {
        RegisterDepartmentRequestBean requestBean = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterDepartmentResponseBean serviceResponse = departmentService.registerDepartement(requestBean);
        log.info("Register department to save in memory and trying the second time, {}", serviceResponse);
        serviceResponse = departmentService.registerDepartement(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.DEPARTMENT_ALREADY_EXISTS, serviceResponse);
    }

    @Test
    @DisplayName("Register room successfully")
    void registerRoom() {
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        departmentService.registerDepartement(requestBeanDepartment);
        RegisterRoomResponseBean serviceResponse = departmentService.registerRoom(requestBeanRoom);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
        assertEquals(requestBeanRoom.getRoomNumber(), serviceResponse.getRoomNumber());
    }

    @Test
    @DisplayName("Register room failing with department not found")
    void registerRoomKODepartmentNotFound() {
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .create();
        RegisterRoomResponseBean serviceResponse = departmentService.registerRoom(requestBeanRoom);

        assertResponseCodeEnum(ResponseCodesEnum.DEPARTMENT_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Register room failing with room already exists")
    void registerRoomKORoomAlreadyExists() {
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        departmentService.registerDepartement(requestBeanDepartment);
        RegisterRoomResponseBean serviceResponse = departmentService.registerRoom(requestBeanRoom);
        log.info("First room saving, {}", serviceResponse);
        serviceResponse = departmentService.registerRoom(requestBeanRoom);

        assertResponseCodeEnum(ResponseCodesEnum.ROOM_ALREADY_EXISTS, serviceResponse);
    }

    @Test
    @DisplayName("Register bed inside room successfully")
    void registerRoomBedOK() {
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .set(field(RegisterBedRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .set(field(RegisterBedRoomRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .create();
        departmentService.registerDepartement(requestBeanDepartment);
        departmentService.registerRoom(requestBeanRoom);
        RegisterBedRoomResponseBean serviceResponse = departmentService.registerBedRoom(requestBeanBed);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
        assertEquals(requestBeanBed.getBedNumber(), serviceResponse.getBedNumber());
    }

    @Test
    @DisplayName("Register bed inside room failing with department not found")
    void registerRoomBedKODepartmentNotFound() {
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .create();
        RegisterBedRoomResponseBean serviceResponse = departmentService.registerBedRoom(requestBeanBed);

        assertResponseCodeEnum(ResponseCodesEnum.DEPARTMENT_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Register bed inside room failing with department room not found")
    void registerRoomBedKORoomNotFound() {
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .set(field(RegisterBedRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        departmentService.registerDepartement(requestBeanDepartment);
        RegisterBedRoomResponseBean serviceResponse = departmentService.registerBedRoom(requestBeanBed);

        assertResponseCodeEnum(ResponseCodesEnum.ROOM_NOT_FOUND, serviceResponse);

    }

    @Test
    @DisplayName("Assign bed to patient successfully")
    void assignBedToPatientOK() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .set(field(RegisterBedRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .set(field(RegisterBedRoomRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .create();
        AssignRoomBedRequestBean requestBean = Instancio.of(AssignRoomBedRequestBean.class)
                .set(field(AssignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(AssignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(AssignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(AssignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        departmentService.registerDepartement(requestBeanDepartment);
        departmentService.registerRoom(requestBeanRoom);
        departmentService.registerBedRoom(requestBeanBed);
        AssignRoomBedResponseBean serviceResponse = departmentService.assignBedToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
        assertEquals(requestBeanBed.getBedNumber(), serviceResponse.getBedNumber());
        assertEquals(requestBeanRoom.getRoomNumber(), serviceResponse.getRoomNumber());
        assertEquals(requestBeanDepartment.getCode(), serviceResponse.getDepartmentCode());
    }

    @Test
    @DisplayName("Assign bed to patient failing with patient not found")
    void assignBedToPatientKOPatientNotFound() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .set(field(RegisterBedRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .set(field(RegisterBedRoomRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .create();
        AssignRoomBedRequestBean requestBean = Instancio.of(AssignRoomBedRequestBean.class)
                .set(field(AssignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(AssignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(AssignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(AssignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        departmentService.registerDepartement(requestBeanDepartment);
        departmentService.registerRoom(requestBeanRoom);
        departmentService.registerBedRoom(requestBeanBed);
        AssignRoomBedResponseBean serviceResponse = departmentService.assignBedToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.PATIENT_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Assign bed to patient failing with department not found")
    void assignBedToPatientKODepartmentNotFound() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .set(field(RegisterBedRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .set(field(RegisterBedRoomRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .create();
        AssignRoomBedRequestBean requestBean = Instancio.of(AssignRoomBedRequestBean.class)
                .set(field(AssignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(AssignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(AssignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(AssignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        AssignRoomBedResponseBean serviceResponse = departmentService.assignBedToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.DEPARTMENT_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Assign bed to patient failing with department room not found")
    void assignBedToPatientKODepartmentRoomNotFound() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .set(field(RegisterBedRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .set(field(RegisterBedRoomRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .create();
        AssignRoomBedRequestBean requestBean = Instancio.of(AssignRoomBedRequestBean.class)
                .set(field(AssignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(AssignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(AssignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(AssignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        departmentService.registerDepartement(requestBeanDepartment);
        AssignRoomBedResponseBean serviceResponse = departmentService.assignBedToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.ROOM_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Assign bed to patient failing with department room bed not found")
    void assignBedToPatientKODepartmentRoomBedNotFound() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .set(field(RegisterBedRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .set(field(RegisterBedRoomRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .create();
        AssignRoomBedRequestBean requestBean = Instancio.of(AssignRoomBedRequestBean.class)
                .set(field(AssignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(AssignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(AssignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(AssignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        departmentService.registerDepartement(requestBeanDepartment);
        departmentService.registerRoom(requestBeanRoom);
        AssignRoomBedResponseBean serviceResponse = departmentService.assignBedToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.BED_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Assign bed to patient failing with department room bed already assigned")
    void assignBedToPatientKODepartmentRoomBedAlreadyAssigned() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .set(field(RegisterBedRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .set(field(RegisterBedRoomRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .create();
        AssignRoomBedRequestBean requestBean = Instancio.of(AssignRoomBedRequestBean.class)
                .set(field(AssignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(AssignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(AssignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(AssignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        departmentService.registerDepartement(requestBeanDepartment);
        departmentService.registerRoom(requestBeanRoom);
        departmentService.registerBedRoom(requestBeanBed);
        AssignRoomBedResponseBean serviceResponse = departmentService.assignBedToPatient(requestBean);
        log.info("First registration bed to patient, {}", serviceResponse);
        serviceResponse = departmentService.assignBedToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.BED_ALREADY_ASSIGNED, serviceResponse);
    }

    @Test
    @DisplayName("Unassign bed to patient successfully")
    void unassignBedToPatientOK() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .set(field(RegisterBedRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .set(field(RegisterBedRoomRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .create();
        AssignRoomBedRequestBean requestBeanAssign = Instancio.of(AssignRoomBedRequestBean.class)
                .set(field(AssignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(AssignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(AssignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(AssignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        UnassignRoomBedRequestBean requestBean = Instancio.of(UnassignRoomBedRequestBean.class)
                .set(field(UnassignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(UnassignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(UnassignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(UnassignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        departmentService.registerDepartement(requestBeanDepartment);
        departmentService.registerRoom(requestBeanRoom);
        departmentService.registerBedRoom(requestBeanBed);
        departmentService.assignBedToPatient(requestBeanAssign);
        UnassignRoomBedResponseBean serviceResponse = departmentService.unassignBedToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
        assertEquals(requestBeanBed.getBedNumber(), serviceResponse.getBedNumber());
        assertEquals(requestBeanRoom.getRoomNumber(), serviceResponse.getRoomNumber());
        assertEquals(requestBeanDepartment.getCode(), serviceResponse.getDepartmentCode());
    }

    @Test
    void unassignBedToPatientKOPatientNotFound() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .set(field(RegisterBedRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .set(field(RegisterBedRoomRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .create();
        AssignRoomBedRequestBean requestBeanAssign = Instancio.of(AssignRoomBedRequestBean.class)
                .set(field(AssignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(AssignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(AssignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(AssignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        UnassignRoomBedRequestBean requestBean = Instancio.of(UnassignRoomBedRequestBean.class)
                .set(field(UnassignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(UnassignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(UnassignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(UnassignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        departmentService.registerDepartement(requestBeanDepartment);
        departmentService.registerRoom(requestBeanRoom);
        departmentService.registerBedRoom(requestBeanBed);
        departmentService.assignBedToPatient(requestBeanAssign);
        UnassignRoomBedResponseBean serviceResponse = departmentService.unassignBedToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.PATIENT_NOT_FOUND, serviceResponse);
    }

    @Test
    void unassignBedToPatientKODepartmentNotFound() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .set(field(RegisterBedRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .set(field(RegisterBedRoomRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .create();
        AssignRoomBedRequestBean requestBeanAssign = Instancio.of(AssignRoomBedRequestBean.class)
                .set(field(AssignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(AssignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(AssignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(AssignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        UnassignRoomBedRequestBean requestBean = Instancio.of(UnassignRoomBedRequestBean.class)
                .set(field(UnassignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(UnassignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(UnassignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(UnassignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        departmentService.registerRoom(requestBeanRoom);
        departmentService.registerBedRoom(requestBeanBed);
        departmentService.assignBedToPatient(requestBeanAssign);
        UnassignRoomBedResponseBean serviceResponse = departmentService.unassignBedToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.DEPARTMENT_NOT_FOUND, serviceResponse);
    }

    @Test
    void unassignBedToPatientKODepartmentRoomNotFound() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .set(field(RegisterBedRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .set(field(RegisterBedRoomRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .create();
        AssignRoomBedRequestBean requestBeanAssign = Instancio.of(AssignRoomBedRequestBean.class)
                .set(field(AssignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(AssignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(AssignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(AssignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        UnassignRoomBedRequestBean requestBean = Instancio.of(UnassignRoomBedRequestBean.class)
                .set(field(UnassignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(UnassignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(UnassignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(UnassignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        departmentService.registerDepartement(requestBeanDepartment);
        departmentService.registerBedRoom(requestBeanBed);
        departmentService.assignBedToPatient(requestBeanAssign);
        UnassignRoomBedResponseBean serviceResponse = departmentService.unassignBedToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.ROOM_NOT_FOUND, serviceResponse);
    }

    @Test
    void unassignBedToPatientKODepartmentRoomBedNotFound() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .set(field(RegisterBedRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .set(field(RegisterBedRoomRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .create();
        AssignRoomBedRequestBean requestBeanAssign = Instancio.of(AssignRoomBedRequestBean.class)
                .set(field(AssignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(AssignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(AssignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(AssignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        UnassignRoomBedRequestBean requestBean = Instancio.of(UnassignRoomBedRequestBean.class)
                .set(field(UnassignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(UnassignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(UnassignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(UnassignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        departmentService.registerDepartement(requestBeanDepartment);
        departmentService.registerRoom(requestBeanRoom);
        departmentService.assignBedToPatient(requestBeanAssign);
        UnassignRoomBedResponseBean serviceResponse = departmentService.unassignBedToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.BED_NOT_FOUND, serviceResponse);
    }

    @Test
    void unassignBedToPatientKOBedIsNotAssigned() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .set(field(RegisterBedRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .set(field(RegisterBedRoomRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .create();
        UnassignRoomBedRequestBean requestBean = Instancio.of(UnassignRoomBedRequestBean.class)
                .set(field(UnassignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(UnassignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(UnassignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(UnassignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        departmentService.registerDepartement(requestBeanDepartment);
        departmentService.registerRoom(requestBeanRoom);
        departmentService.registerBedRoom(requestBeanBed);
        UnassignRoomBedResponseBean serviceResponse = departmentService.unassignBedToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.BED_NOT_ASSIGNED, serviceResponse);
    }

    @Test
    void unassignBedToPatientKOBedIsNotAssignedToGivenPatient() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        RegisterPatientRequestBean requestBeanPatientSecond = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Elenoire Francesca")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Tedesco")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2002, 8, 2))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "TDSLRF02M42L049Q")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.FEMALE.getGenderCode())
                .create();
        RegisterDepartmentRequestBean requestBeanDepartment = Instancio.of(RegisterDepartmentRequestBean.class)
                .set(field(RegisterDepartmentRequestBean::getEmail), "department@mail.hospital.org")
                .set(field(RegisterDepartmentRequestBean::getPhone), "+393333333333")
                .create();
        RegisterRoomRequestBean requestBeanRoom = Instancio.of(RegisterRoomRequestBean.class)
                .set(field(RegisterRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        RegisterBedRoomRequestBean requestBeanBed = Instancio.of(RegisterBedRoomRequestBean.class)
                .set(field(RegisterBedRoomRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .set(field(RegisterBedRoomRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .create();
        AssignRoomBedRequestBean requestBeanAssign = Instancio.of(AssignRoomBedRequestBean.class)
                .set(field(AssignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(AssignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(AssignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(AssignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        UnassignRoomBedRequestBean requestBean = Instancio.of(UnassignRoomBedRequestBean.class)
                .set(field(UnassignRoomBedRequestBean::getPatientFiscalCode), requestBeanPatientSecond.getPatientFiscalCode())
                .set(field(UnassignRoomBedRequestBean::getBedNumber), requestBeanBed.getBedNumber())
                .set(field(UnassignRoomBedRequestBean::getRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(UnassignRoomBedRequestBean::getDepartmentCode), requestBeanDepartment.getCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        patientService.registerNewPatient(requestBeanPatientSecond);
        departmentService.registerDepartement(requestBeanDepartment);
        departmentService.registerRoom(requestBeanRoom);
        departmentService.registerBedRoom(requestBeanBed);
        departmentService.assignBedToPatient(requestBeanAssign);
        UnassignRoomBedResponseBean serviceResponse = departmentService.unassignBedToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.PATIENT_NOT_ASSIGNED_TO_GIVEN_BED, serviceResponse);
    }
}