package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.CreateExamMachineRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateExamTypeRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateExamMachineStatusRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateExamMachineResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateExamTypeResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UpdateExamMachineStatusResponseBean;
import com.newhorizon.nhmonolithicapplication.data.ExamMachineDAO;
import com.newhorizon.nhmonolithicapplication.data.ExamTypeDAO;
import com.newhorizon.nhmonolithicapplication.dto.ExamMachineDTO;
import com.newhorizon.nhmonolithicapplication.dto.ExamTypeDTO;
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
public class ExamInventoryServiceImpl implements ExamInventoryService {
    private final ExamTypeDAO examTypeDAO;
    private final ExamMachineDAO examMachineDAO;

    @Override
    @SuppressWarnings("unchecked")
    public CreateExamTypeResponseBean createNewExamType(CreateExamTypeRequestBean requestBean) {
        if (examTypeDAO.findByCode(requestBean.getExamCode()).isPresent()) {
            return CreateExamTypeResponseBean.builder()
                    .status(ResponseCodesEnum.EXAM_CODE_ALREADY_EXISTS.getStatus())
                    .responseCode(ResponseCodesEnum.EXAM_CODE_ALREADY_EXISTS.getErrorCode())
                    .responseMessage(ResponseCodesEnum.EXAM_CODE_ALREADY_EXISTS.getDescription())
                    .build();
        }
        Optional<ExamTypeDTO> savedExamType = examTypeDAO.createExamType(ExamTypeDTO.builder()
                .examCode(requestBean.getExamCode())
                .examName(requestBean.getExamName())
                .examDescription(requestBean.getDescription())
                .build());
        if (savedExamType.isEmpty()) {
            return CreateExamTypeResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return CreateExamTypeResponseBean.builder()
                .examType(savedExamType.get().getExamCode())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CreateExamMachineResponseBean createNewExamMachine(CreateExamMachineRequestBean requestBean) {
        if (examTypeDAO.findByCode(requestBean.getExamTypeCode()).isEmpty()) {
            return CreateExamMachineResponseBean.builder()
                    .status(ResponseCodesEnum.EXAM_CODE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.EXAM_CODE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.EXAM_CODE_NOT_FOUND.getDescription())
                    .build();
        }
        if (examMachineDAO.findBySerialNumber(requestBean.getMachineSerialNumber()).isPresent()) {
            return CreateExamMachineResponseBean.builder()
                    .status(ResponseCodesEnum.MACHINE_ALREADY_EXISTS.getStatus())
                    .responseCode(ResponseCodesEnum.MACHINE_ALREADY_EXISTS.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MACHINE_ALREADY_EXISTS.getDescription())
                    .build();
        }
        Optional<ExamMachineDTO> savedExamMachine = examMachineDAO.createExamMachine(ExamMachineDTO.builder()
                .examCode(requestBean.getExamTypeCode())
                .machineSerialNumber(requestBean.getMachineSerialNumber())
                .machineName(requestBean.getMachineName())
                .build());
        if (savedExamMachine.isEmpty()) {
            return CreateExamMachineResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return CreateExamMachineResponseBean.builder()
                .serialNumber(savedExamMachine.get().getMachineSerialNumber())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public UpdateExamMachineStatusResponseBean updateMachineStatus(UpdateExamMachineStatusRequestBean requestBean) {
        if (examMachineDAO.findById(requestBean.getMachineId()).isEmpty()) {
            return UpdateExamMachineStatusResponseBean.builder()
                    .status(ResponseCodesEnum.MACHINE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MACHINE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MACHINE_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<ExamMachineDTO> savedExamMachine = examMachineDAO.toggleMachineEnablement(requestBean.getMachineId(),
                requestBean.getIsEnabled());
        if (savedExamMachine.isEmpty()) {
            return UpdateExamMachineStatusResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return UpdateExamMachineStatusResponseBean.builder()
                .isEnabled(requestBean.getIsEnabled())
                .build();
    }
}
