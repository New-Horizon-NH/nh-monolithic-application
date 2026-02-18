package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.NhMonolithicApplicationTest;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateSurgeryRoomRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateSurgeryTypeRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.GenerateMedicalChartRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterPatientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.ScheduleSurgeryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnscheduleSurgeryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateSurgeryRoomResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateSurgeryTypeResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.ScheduleSurgeryResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UnscheduleSurgeryResponseBean;
import com.newhorizon.nhmonolithicapplication.enums.GenderTypeEnum;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import com.newhorizon.nhmonolithicapplication.enums.SurgicalRoomTypeEnum;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.instancio.Select.field;

@Slf4j
@Transactional
class SurgeryServiceTest extends NhMonolithicApplicationTest {
    @Autowired
    SurgeryServiceImpl surgeryService;
    @Autowired
    PatientServiceImpl patientService;
    @Autowired
    MedicalChartServiceImpl medicalChartService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createSurgeryRoom() {
        CreateSurgeryRoomRequestBean requestBean = Instancio.of(CreateSurgeryRoomRequestBean.class)
                .set(field(CreateSurgeryRoomRequestBean::getRoomType), SurgicalRoomTypeEnum.CARDIAC_SURGERY.getSurgicalRoomTypeCode())
                .create();
        CreateSurgeryRoomResponseBean serviceResponse = surgeryService.createSurgeryRoom(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    void createSurgeryType() {
        CreateSurgeryTypeRequestBean requestBean = Instancio.of(CreateSurgeryTypeRequestBean.class)
                .create();
        CreateSurgeryTypeResponseBean serviceResponse = surgeryService.createSurgeryType(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    void scheduleSurgery() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        GenerateMedicalChartRequestBean requestBeanChart = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        medicalChartService.generateMedicalChart(requestBeanChart);
        CreateSurgeryRoomRequestBean requestBeanRoom = Instancio.of(CreateSurgeryRoomRequestBean.class)
                .set(field(CreateSurgeryRoomRequestBean::getRoomType), SurgicalRoomTypeEnum.CARDIAC_SURGERY.getSurgicalRoomTypeCode())
                .create();
        surgeryService.createSurgeryRoom(requestBeanRoom);
        CreateSurgeryTypeRequestBean requestBeanSurgeryType = Instancio.of(CreateSurgeryTypeRequestBean.class)
                .create();
        surgeryService.createSurgeryType(requestBeanSurgeryType);
        ScheduleSurgeryRequestBean requestBean = Instancio.of(ScheduleSurgeryRequestBean.class)
                .set(field(ScheduleSurgeryRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(ScheduleSurgeryRequestBean::getSurgicalRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(ScheduleSurgeryRequestBean::getSurgeryTypeCode), requestBeanSurgeryType.getSurgeryTypeCode())
                .create();
        requestBean.setSurgeryEnd(requestBean.getSurgeryStart().plusSeconds(3600));
        ScheduleSurgeryResponseBean serviceResponse = surgeryService.scheduleSurgery(requestBean);
        log.info(serviceResponse.toString());

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    void unscheduleSurgery() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        GenerateMedicalChartRequestBean requestBeanChart = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        medicalChartService.generateMedicalChart(requestBeanChart);
        CreateSurgeryRoomRequestBean requestBeanRoom = Instancio.of(CreateSurgeryRoomRequestBean.class)
                .set(field(CreateSurgeryRoomRequestBean::getRoomType), SurgicalRoomTypeEnum.CARDIAC_SURGERY.getSurgicalRoomTypeCode())
                .create();
        surgeryService.createSurgeryRoom(requestBeanRoom);
        CreateSurgeryTypeRequestBean requestBeanSurgeryType = Instancio.of(CreateSurgeryTypeRequestBean.class)
                .create();
        surgeryService.createSurgeryType(requestBeanSurgeryType);
        ScheduleSurgeryRequestBean requestBeanSchedule = Instancio.of(ScheduleSurgeryRequestBean.class)
                .set(field(ScheduleSurgeryRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(ScheduleSurgeryRequestBean::getSurgicalRoomNumber), requestBeanRoom.getRoomNumber())
                .set(field(ScheduleSurgeryRequestBean::getSurgeryTypeCode), requestBeanSurgeryType.getSurgeryTypeCode())
                .create();
        String surgeryId = surgeryService.scheduleSurgery(requestBeanSchedule).getSurgeryId();
        UnscheduleSurgeryRequestBean requestBean = Instancio.of(UnscheduleSurgeryRequestBean.class)
                .set(field(UnscheduleSurgeryRequestBean::getSurgeryId), surgeryId)
                .create();
        UnscheduleSurgeryResponseBean serviceResponse = surgeryService.unscheduleSurgery(requestBean);
        log.info(requestBean.toString());
        log.info(serviceResponse.toString());

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    void rescheduleSurgery() {
    }
}