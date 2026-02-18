package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.NhMonolithicApplicationTest;
import com.newhorizon.nhmonolithicapplication.beans.request.GenerateMedicalChartRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterPatientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterScaleRecordRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveScaleListRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterScaleRecordResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrieveScaleListResponseBean;
import com.newhorizon.nhmonolithicapplication.enums.GenderTypeEnum;
import com.newhorizon.nhmonolithicapplication.enums.NursingRatingScaleEnum;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
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
class NursingRatingScaleServiceTest extends NhMonolithicApplicationTest {
    @Autowired
    NursingRatingScaleServiceImpl nursingRatingScaleService;
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
    void createRecord() {
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
        RegisterScaleRecordRequestBean requestBean = Instancio.of(RegisterScaleRecordRequestBean.class)
                .set(field(RegisterScaleRecordRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(RegisterScaleRecordRequestBean::getScaleCode), NursingRatingScaleEnum.BRISTOL.getNursingScaleCode())
                .set(field(RegisterScaleRecordRequestBean::getValue), 6)
                .create();
        RegisterScaleRecordResponseBean serviceResponse = nursingRatingScaleService.createRecord(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    void retrieveMedicalChartScaleList() {
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
        String medicalChartId = medicalChartService.generateMedicalChart(requestBeanChart).getMedicalChart();
        RegisterScaleRecordRequestBean requestBeanScale = Instancio.of(RegisterScaleRecordRequestBean.class)
                .set(field(RegisterScaleRecordRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(RegisterScaleRecordRequestBean::getScaleCode), NursingRatingScaleEnum.BRISTOL.getNursingScaleCode())
                .set(field(RegisterScaleRecordRequestBean::getValue), 6)
                .create();
        nursingRatingScaleService.createRecord(requestBeanScale);
        RetrieveScaleListRequestBean requestBean = Instancio.of(RetrieveScaleListRequestBean.class)
                .set(field(RetrieveScaleListRequestBean::getMedicalChartId), medicalChartId)
                .create();
        RetrieveScaleListResponseBean serviceResponse = nursingRatingScaleService.retrieveMedicalChartScaleList(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }
}