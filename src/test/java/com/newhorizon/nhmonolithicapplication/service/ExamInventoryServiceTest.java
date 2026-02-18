package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.NhMonolithicApplicationTest;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateExamMachineRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateExamTypeRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateExamMachineStatusRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateExamMachineResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateExamTypeResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UpdateExamMachineStatusResponseBean;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.ExamMachineRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.instancio.Select.field;

@Slf4j
@Transactional
class ExamInventoryServiceTest extends NhMonolithicApplicationTest {
    @Autowired
    ExamInventoryServiceImpl examInventoryService;
    @Autowired
    ExamMachineRepo examMachineRepo;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Create exam type successfully")
    void createNewExamTypeOK() {
        CreateExamTypeRequestBean requestBean = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        CreateExamTypeResponseBean serviceResponse = examInventoryService.createNewExamType(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    @DisplayName("Create exam type failing with exam code already exists")
    void createNewExamTypeKOExamAlreadyExists() {
        CreateExamTypeRequestBean requestBean = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        CreateExamTypeResponseBean serviceResponse = examInventoryService.createNewExamType(requestBean);
        log.info(serviceResponse.toString());
        serviceResponse = examInventoryService.createNewExamType(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.EXAM_CODE_ALREADY_EXISTS, serviceResponse);
    }

    @Test
    @DisplayName("Create exam machine successfully")
    void createNewExamMachineOK() {
        CreateExamTypeRequestBean requestBeanExamType = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        examInventoryService.createNewExamType(requestBeanExamType);
        CreateExamMachineRequestBean requestBean = Instancio.of(CreateExamMachineRequestBean.class)
                .set(field(CreateExamMachineRequestBean::getExamTypeCode), requestBeanExamType.getExamCode())
                .create();
        CreateExamMachineResponseBean serviceResponse = examInventoryService.createNewExamMachine(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    @DisplayName("Create exam machine failing with exam code not found")
    void createNewExamMachineKOCodeNotFound() {
        CreateExamMachineRequestBean requestBean = Instancio.of(CreateExamMachineRequestBean.class)
                .create();
        CreateExamMachineResponseBean serviceResponse = examInventoryService.createNewExamMachine(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.EXAM_CODE_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Create exam machine failing with machine already exists")
    void createNewExamMachineKOMachineAlreadyExists() {
        CreateExamTypeRequestBean requestBeanExamType = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        examInventoryService.createNewExamType(requestBeanExamType);
        CreateExamMachineRequestBean requestBean = Instancio.of(CreateExamMachineRequestBean.class)
                .set(field(CreateExamMachineRequestBean::getExamTypeCode), requestBeanExamType.getExamCode())
                .create();
        CreateExamMachineResponseBean serviceResponse = examInventoryService.createNewExamMachine(requestBean);
        log.info(serviceResponse.toString());
        serviceResponse = examInventoryService.createNewExamMachine(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.MACHINE_ALREADY_EXISTS, serviceResponse);
    }

    @Test
    @DisplayName("Update machine status successfully")
    void updateMachineStatusOK() {
        CreateExamTypeRequestBean requestBeanExamType = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        examInventoryService.createNewExamType(requestBeanExamType);
        CreateExamMachineRequestBean requestBeanMachine = Instancio.of(CreateExamMachineRequestBean.class)
                .set(field(CreateExamMachineRequestBean::getExamTypeCode), requestBeanExamType.getExamCode())
                .create();
        String serialNumber = examInventoryService.createNewExamMachine(requestBeanMachine).getSerialNumber();
        String machineId = examMachineRepo.findByMachineSerialNumber(serialNumber).get().getId();

        UpdateExamMachineStatusRequestBean requestBean = Instancio.of(UpdateExamMachineStatusRequestBean.class)
                .set(field(UpdateExamMachineStatusRequestBean::getMachineId), machineId)
                .create();
        UpdateExamMachineStatusResponseBean serviceResponse = examInventoryService.updateMachineStatus(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    @DisplayName("Update machine status failing with machine not found")
    void updateMachineStatusKOMachineNotFound() {
        UpdateExamMachineStatusRequestBean requestBean = Instancio.of(UpdateExamMachineStatusRequestBean.class)
                .create();
        UpdateExamMachineStatusResponseBean serviceResponse = examInventoryService.updateMachineStatus(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.MACHINE_NOT_FOUND, serviceResponse);
    }


}