package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.AddMedicalTeamMemberRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.AssignMemberToPatientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveCurrentlyFollowedPatientListRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.AddMedicalTeamMemberResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.AssignMemberToPatientResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrieveCurrentlyFollowedPatientListResponseBean;
import com.newhorizon.nhmonolithicapplication.data.MedicalChartDAO;
import com.newhorizon.nhmonolithicapplication.data.MedicalTeamDAO;
import com.newhorizon.nhmonolithicapplication.data.PatientDAO;
import com.newhorizon.nhmonolithicapplication.dto.MedicalChartDTO;
import com.newhorizon.nhmonolithicapplication.dto.MedicalChartMedicalTeamAssociationDTO;
import com.newhorizon.nhmonolithicapplication.dto.MedicalTeamDTO;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class MedicalTeamServiceImpl implements MedicalTeamService {
    private final MedicalTeamDAO medicalTeamDAO;
    private final PatientDAO patientDAO;
    private final MedicalChartDAO medicalChartDAO;

    @Override
    @SuppressWarnings("unchecked")
    public AddMedicalTeamMemberResponseBean addMember(AddMedicalTeamMemberRequestBean requestBean) {
        if (medicalTeamDAO.retrieveByFiscalCode(requestBean.getFiscalCode()).isPresent()) {
            return AddMedicalTeamMemberResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_MEMBER_ALREADY_EXISTS.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_MEMBER_ALREADY_EXISTS.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_MEMBER_ALREADY_EXISTS.getDescription())
                    .build();
        }
        Optional<MedicalTeamDTO> saved = medicalTeamDAO.createMember(MedicalTeamDTO.builder()
                .fiscalCode(requestBean.getFiscalCode())
                .name(requestBean.getName())
                .surname(requestBean.getSurname())
                .commaSeparatedRoles(String.join(",", requestBean.getRoles()))
                .build());
        if (saved.isEmpty()) {
            return AddMedicalTeamMemberResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return AddMedicalTeamMemberResponseBean.builder()
                .memberId(saved.get().getMemberId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public AssignMemberToPatientResponseBean assignMemberToPatient(AssignMemberToPatientRequestBean requestBean) {
        Optional<MedicalTeamDTO> medicalTeamDTO = medicalTeamDAO.retrieveByFiscalCode(requestBean.getMedicalTeamMemberFiscalCode());
        if (medicalTeamDTO.isEmpty()) {
            return AssignMemberToPatientResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getDescription())
                    .build();
        }
        if (patientDAO.findPatientByFiscalCode(requestBean.getPatientFiscalCode()).isEmpty()) {
            return AssignMemberToPatientResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<MedicalChartDTO> medicalChartDTO = medicalChartDAO.retrieveOpenMedicalChart(requestBean.getPatientFiscalCode());
        if (medicalChartDTO.isEmpty()) {
            return AssignMemberToPatientResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<MedicalChartMedicalTeamAssociationDTO> saved = medicalTeamDAO.createPatientAssociation(MedicalChartMedicalTeamAssociationDTO.builder()
                .medicalTeamId(medicalTeamDTO.get().getMemberId())
                .medicalChartId(medicalChartDTO.get().getMedicalChartId())
                .build());
        if (saved.isEmpty()) {
            return AssignMemberToPatientResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return AssignMemberToPatientResponseBean.builder()
                .associationId(saved.get().getId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RetrieveCurrentlyFollowedPatientListResponseBean retrieveCurrentlyFollowedPatientList(RetrieveCurrentlyFollowedPatientListRequestBean requestBean) {
        if (medicalTeamDAO.retrieveByFiscalCode(requestBean.getLoggedMemberFiscalCode()).isEmpty()) {
            return RetrieveCurrentlyFollowedPatientListResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getDescription())
                    .build();
        }
        return RetrieveCurrentlyFollowedPatientListResponseBean.builder()
                .medicalChartList(medicalChartDAO.retrieveFollowedByHospitalMemberFiscalCode(requestBean.getLoggedMemberFiscalCode())
                        .stream()
                        .map(MedicalChartDTO::getMedicalChartId)
                        .toList())
                .build();
    }
}
