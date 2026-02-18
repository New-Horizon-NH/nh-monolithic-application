package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.AutoGenerateMonthlyWorkShiftRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveMonthlyShiftsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateShiftRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.GenerateMonthlyWorkShiftResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrieveMonthlyShiftsResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UpdateShiftResponseBean;
import com.newhorizon.nhmonolithicapplication.data.MedicalDAO;
import com.newhorizon.nhmonolithicapplication.data.MedicalWorkShiftAssociationDAO;
import com.newhorizon.nhmonolithicapplication.data.WorkShiftDAO;
import com.newhorizon.nhmonolithicapplication.dto.MedicalWorkShiftAssociationDTO;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import com.newhorizon.nhmonolithicapplication.enums.WorkShiftEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WorkShiftServiceImpl implements WorkShiftService {
    private final MedicalDAO medicalDAO;
    private final MedicalWorkShiftAssociationDAO medicalWorkShiftAssociationDAO;
    private final WorkShiftDAO workShiftDAO;

    @Override
    @SuppressWarnings("unchecked")
    public GenerateMonthlyWorkShiftResponseBean autogenerateWorkShift(AutoGenerateMonthlyWorkShiftRequestBean requestBean) {
        if (medicalDAO.retrieveById(requestBean.getMedicalId()).isEmpty()) {
            return GenerateMonthlyWorkShiftResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getDescription())
                    .build();
        }
        WorkShiftEnum initialShift = WorkShiftEnum.retrieveByCode(requestBean.getInitialShiftCode());
        if (isNull(initialShift)) {
            return GenerateMonthlyWorkShiftResponseBean.builder()
                    .status(ResponseCodesEnum.WORK_SHIFT_CODE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.WORK_SHIFT_CODE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.WORK_SHIFT_CODE_NOT_FOUND.getDescription())
                    .build();
        }
        List<WorkShiftEnum> shifts = WorkShiftEnum.workShiftFromInitial(initialShift,
                calculateDaysInMonth(requestBean.getMonth(), requestBean.getYear()));
        List<String> shiftAssociations = IntStream.range(0, shifts.size())
                .mapToObj(index -> {
                    Optional<MedicalWorkShiftAssociationDTO> associationDTO = medicalWorkShiftAssociationDAO.createAssociation(MedicalWorkShiftAssociationDTO.builder()
                            .shiftDate(LocalDate.of(requestBean.getYear(), requestBean.getMonth(), index + 1))
                            .medicalId(requestBean.getMedicalId())
                            .workShiftId(workShiftDAO.retrieveByShiftCode(requestBean.getInitialShiftCode()).orElseThrow().getWorkShiftId())
                            .build());
                    return associationDTO.map(MedicalWorkShiftAssociationDTO::getAssociationId).orElse(null);
                })
                .toList();
        if (shiftAssociations.stream().anyMatch(Objects::isNull)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return GenerateMonthlyWorkShiftResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return GenerateMonthlyWorkShiftResponseBean.builder()
                .associations(shiftAssociations)
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RetrieveMonthlyShiftsResponseBean retrieveMonthlyShifts(RetrieveMonthlyShiftsRequestBean requestBean) {
        if (medicalDAO.retrieveById(requestBean.getMedicalId()).isEmpty()) {
            return RetrieveMonthlyShiftsResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getDescription())
                    .build();
        }
        List<RetrieveMonthlyShiftsResponseBean.ShiftResponse> shifts = medicalWorkShiftAssociationDAO.findMonthlyAssociation(requestBean.getMedicalId(),
                        requestBean.getMonth(),
                        requestBean.getYear())
                .stream()
                .map(association -> RetrieveMonthlyShiftsResponseBean.ShiftResponse.builder()
                        .shiftId(association.getAssociationId())
                        .day(association.getShiftDate().getDayOfMonth())
                        .shiftName(WorkShiftEnum.retrieveByCode(workShiftDAO.findById(association.getWorkShiftId())
                                        .orElseThrow()
                                        .getShiftCode())
                                .getWorkShiftName())
                        .build())
                .toList();
        return RetrieveMonthlyShiftsResponseBean.builder()
                .shifts(shifts)
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public UpdateShiftResponseBean updateShift(UpdateShiftRequestBean requestBean) {
        if (medicalWorkShiftAssociationDAO.findAssociation(requestBean.getAssociationShiftId()).isEmpty()) {
            return UpdateShiftResponseBean.builder()
                    .status(ResponseCodesEnum.WORK_SHIFT_ASSOCIATION_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.WORK_SHIFT_ASSOCIATION_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.WORK_SHIFT_ASSOCIATION_NOT_FOUND.getDescription())
                    .build();
        }
        if (isNull(WorkShiftEnum.retrieveByCode(requestBean.getNewShiftCode()))) {
            return UpdateShiftResponseBean.builder()
                    .status(ResponseCodesEnum.WORK_SHIFT_CODE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.WORK_SHIFT_CODE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.WORK_SHIFT_CODE_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<MedicalWorkShiftAssociationDTO> medicalWorkShiftAssociationDTO = medicalWorkShiftAssociationDAO.updateAssociation(MedicalWorkShiftAssociationDTO.builder()
                .associationId(requestBean.getAssociationShiftId())
                .workShiftId(workShiftDAO.retrieveByShiftCode(requestBean.getNewShiftCode()).orElseThrow().getWorkShiftId())
                .build());
        if (medicalWorkShiftAssociationDTO.isEmpty()) {
            return UpdateShiftResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return UpdateShiftResponseBean.builder()
                .associationId(medicalWorkShiftAssociationDTO.get().getAssociationId())
                .build();
    }

    public Integer calculateDaysInMonth(Integer month, Integer year) {
        return YearMonth.of(year, month).lengthOfMonth();
    }
}
