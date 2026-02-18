package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.NhMonolithicApplicationTest;
import com.newhorizon.nhmonolithicapplication.beans.request.AddMedicalTeamMemberRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.AutoGenerateMonthlyWorkShiftRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveMonthlyShiftsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateShiftRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.GenerateMonthlyWorkShiftResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrieveMonthlyShiftsResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UpdateShiftResponseBean;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import com.newhorizon.nhmonolithicapplication.enums.WorkShiftEnum;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Transactional
class WorkShiftServiceTest extends NhMonolithicApplicationTest {
    @Autowired
    WorkShiftServiceImpl workShiftService;
    @Autowired
    MedicalTeamServiceImpl medicalTeamService;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Autogenerate monthly shifts successfully")
    void autogenerateWorkShift() {
        AddMedicalTeamMemberRequestBean requestBeanMedical = Instancio.of(AddMedicalTeamMemberRequestBean.class)
                .create();
        String medicalId = medicalTeamService.addMember(requestBeanMedical).getMemberId();
        AutoGenerateMonthlyWorkShiftRequestBean requestBean = Instancio.of(AutoGenerateMonthlyWorkShiftRequestBean.class)
                .set(field(AutoGenerateMonthlyWorkShiftRequestBean::getMedicalId), medicalId)
                .set(field(AutoGenerateMonthlyWorkShiftRequestBean::getInitialShiftCode), WorkShiftEnum.NIGHT.getWorkShiftCode())
                .set(field(AutoGenerateMonthlyWorkShiftRequestBean::getMonth), 11)
                .set(field(AutoGenerateMonthlyWorkShiftRequestBean::getYear), 2024)
                .create();
        GenerateMonthlyWorkShiftResponseBean serviceResponse = workShiftService.autogenerateWorkShift(requestBean);
        log.info(serviceResponse.toString());

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
        assertEquals(30, serviceResponse.getAssociations().size());
    }

    @Test
    void retrieveMonthlyShifts() {
        AddMedicalTeamMemberRequestBean requestBeanMedical = Instancio.of(AddMedicalTeamMemberRequestBean.class)
                .create();
        String medicalId = medicalTeamService.addMember(requestBeanMedical).getMemberId();
        AutoGenerateMonthlyWorkShiftRequestBean requestBeanGenerate = Instancio.of(AutoGenerateMonthlyWorkShiftRequestBean.class)
                .set(field(AutoGenerateMonthlyWorkShiftRequestBean::getMedicalId), medicalId)
                .set(field(AutoGenerateMonthlyWorkShiftRequestBean::getInitialShiftCode), WorkShiftEnum.NIGHT.getWorkShiftCode())
                .set(field(AutoGenerateMonthlyWorkShiftRequestBean::getMonth), 11)
                .set(field(AutoGenerateMonthlyWorkShiftRequestBean::getYear), 2024)
                .create();
        workShiftService.autogenerateWorkShift(requestBeanGenerate);
        RetrieveMonthlyShiftsRequestBean requestBean = Instancio.of(RetrieveMonthlyShiftsRequestBean.class)
                .set(field(RetrieveMonthlyShiftsRequestBean::getMedicalId), medicalId)
                .set(field(RetrieveMonthlyShiftsRequestBean::getMonth), 11)
                .set(field(RetrieveMonthlyShiftsRequestBean::getYear), 2024)
                .create();
        RetrieveMonthlyShiftsResponseBean serviceResponse = workShiftService.retrieveMonthlyShifts(requestBean);
        log.info(serviceResponse.toString());

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
        assertEquals(30, serviceResponse.getShifts().size());
    }

    @Test
    void updateShift() {
        AddMedicalTeamMemberRequestBean requestBeanMedical = Instancio.of(AddMedicalTeamMemberRequestBean.class)
                .create();
        String medicalId = medicalTeamService.addMember(requestBeanMedical).getMemberId();
        AutoGenerateMonthlyWorkShiftRequestBean requestBeanGenerate = Instancio.of(AutoGenerateMonthlyWorkShiftRequestBean.class)
                .set(field(AutoGenerateMonthlyWorkShiftRequestBean::getMedicalId), medicalId)
                .set(field(AutoGenerateMonthlyWorkShiftRequestBean::getInitialShiftCode), WorkShiftEnum.NIGHT.getWorkShiftCode())
                .set(field(AutoGenerateMonthlyWorkShiftRequestBean::getMonth), 11)
                .set(field(AutoGenerateMonthlyWorkShiftRequestBean::getYear), 2024)
                .create();
        GenerateMonthlyWorkShiftResponseBean responseBeanShifts = workShiftService.autogenerateWorkShift(requestBeanGenerate);

        UpdateShiftRequestBean requestBean = Instancio.of(UpdateShiftRequestBean.class)
                .set(field(UpdateShiftRequestBean::getAssociationShiftId), responseBeanShifts.getAssociations().get(10))
                .set(field(UpdateShiftRequestBean::getNewShiftCode), WorkShiftEnum.NIGHT.getWorkShiftCode())
                .create();
        UpdateShiftResponseBean serviceResponse = workShiftService.updateShift(requestBean);
        log.info(serviceResponse.toString());

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }
}