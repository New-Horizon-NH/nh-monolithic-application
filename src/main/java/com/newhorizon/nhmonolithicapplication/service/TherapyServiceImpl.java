package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.AssignTreatmentToTherapyRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CheckTherapyEntryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateSUTRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.AssignTreatmentToTherapyResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CheckTherapyEntryResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateSUTResponseBean;
import com.newhorizon.nhmonolithicapplication.data.DrugPackageDAO;
import com.newhorizon.nhmonolithicapplication.data.MedicalChartDAO;
import com.newhorizon.nhmonolithicapplication.data.MedicalDAO;
import com.newhorizon.nhmonolithicapplication.data.PatientDAO;
import com.newhorizon.nhmonolithicapplication.data.TherapyAdministrationRegistryDAO;
import com.newhorizon.nhmonolithicapplication.data.TherapyDAO;
import com.newhorizon.nhmonolithicapplication.data.TherapyRecordDAO;
import com.newhorizon.nhmonolithicapplication.dto.MedicalChartDTO;
import com.newhorizon.nhmonolithicapplication.dto.TherapyAdministrationRegistryDTO;
import com.newhorizon.nhmonolithicapplication.dto.TherapyDTO;
import com.newhorizon.nhmonolithicapplication.dto.TherapyRecordDTO;
import com.newhorizon.nhmonolithicapplication.enums.AdministrationStatusEnum;
import com.newhorizon.nhmonolithicapplication.enums.AdministrationTypeEnum;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TherapyServiceImpl implements TherapyService {
    private final PatientDAO patientDAO;
    private final MedicalChartDAO medicalChartDAO;
    private final MedicalDAO medicalDAO;
    private final DrugPackageDAO drugPackageDAO;
    private final TherapyDAO therapyDAO;
    private final TherapyRecordDAO therapyRecordDAO;
    private final TherapyAdministrationRegistryDAO therapyAdministrationRegistryDAO;

    @Override
    @SuppressWarnings("unchecked")
    public CreateSUTResponseBean createSUT(CreateSUTRequestBean requestBean) {
        if (patientDAO.findPatientByFiscalCode(requestBean.getPatientFiscalCode()).isEmpty()) {
            return CreateSUTResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription())
                    .build();
        }
        if (medicalDAO.retrieveById(requestBean.getMedicalId()).isEmpty()) {
            return CreateSUTResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<MedicalChartDTO> medicalChartDTO = medicalChartDAO.retrieveOpenMedicalChart(requestBean.getPatientFiscalCode());
        if (medicalChartDTO.isEmpty()) {
            return CreateSUTResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<TherapyDTO> saved = therapyDAO.createSUT(TherapyDTO.builder()
                .medicalChartId(medicalChartDTO.get().getMedicalChartId())
                .medicalCreatorId(requestBean.getMedicalId())
                .build());
        if (saved.isEmpty()) {
            return CreateSUTResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return CreateSUTResponseBean.builder()
                .sutId(saved.get().getSutId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public AssignTreatmentToTherapyResponseBean assignTreatmentToSUT(AssignTreatmentToTherapyRequestBean requestBean) {
        if (therapyDAO.retrieveById(requestBean.getSutId()).isEmpty()) {
            return AssignTreatmentToTherapyResponseBean.builder()
                    .status(ResponseCodesEnum.SUT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.SUT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.SUT_NOT_FOUND.getDescription())
                    .build();
        }
        if (medicalDAO.retrieveById(requestBean.getMedicalAssigneeId()).isEmpty()) {
            return AssignTreatmentToTherapyResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getDescription())
                    .build();
        }
        if (drugPackageDAO.retrieveByPackageId(requestBean.getAdministrationPackageId()).isEmpty()) {
            return AssignTreatmentToTherapyResponseBean.builder()
                    .status(ResponseCodesEnum.DRUG_PACKAGE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.DRUG_PACKAGE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DRUG_PACKAGE_NOT_FOUND.getDescription())
                    .build();
        }
        if (isNull(AdministrationTypeEnum.getByCode(requestBean.getAdministrationType()))) {
            return AssignTreatmentToTherapyResponseBean.builder()
                    .status(ResponseCodesEnum.ADMINISTRATION_TYPE_CODE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.ADMINISTRATION_TYPE_CODE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.ADMINISTRATION_TYPE_CODE_NOT_FOUND.getDescription())
                    .build();
        }
        if (Boolean.TRUE
                .equals(therapyRecordDAO.isPackageAlreadyAssigned(requestBean.getSutId(),
                        requestBean.getAdministrationPackageId()))) {
            return AssignTreatmentToTherapyResponseBean.builder()
                    .status(ResponseCodesEnum.TREATMENT_ALREADY_IN_SUT.getStatus())
                    .responseCode(ResponseCodesEnum.TREATMENT_ALREADY_IN_SUT.getErrorCode())
                    .responseMessage(ResponseCodesEnum.TREATMENT_ALREADY_IN_SUT.getDescription())
                    .build();
        }
        Optional<TherapyRecordDTO> saved = therapyRecordDAO.assignTreatment(TherapyRecordDTO.builder()
                .therapyId(requestBean.getSutId())
                .packageId(requestBean.getAdministrationPackageId())
                .administrationNumber(requestBean.getAdministrationNumber())
                .administrationType(requestBean.getAdministrationType())
                .medicalAssigneeId(requestBean.getMedicalAssigneeId())
                .build());
        if (saved.isEmpty()) {
            return AssignTreatmentToTherapyResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return AssignTreatmentToTherapyResponseBean.builder()
                .assignmentId(saved.get().getTreatmentId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CheckTherapyEntryResponseBean checkTreatement(CheckTherapyEntryRequestBean requestBean) {
        Optional<TherapyRecordDTO> therapyRecordDTO = therapyRecordDAO.retrieveById(requestBean.getTherapyRecordId());
        if (therapyRecordDTO.isEmpty()) {
            return CheckTherapyEntryResponseBean.builder()
                    .status(ResponseCodesEnum.THERAPY_ENTRY_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.THERAPY_ENTRY_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.THERAPY_ENTRY_NOT_FOUND.getDescription())
                    .build();
        }
        if (medicalDAO.retrieveById(requestBean.getAdministratorId()).isEmpty()) {
            return CheckTherapyEntryResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND.getDescription())
                    .build();
        }
        if (isNull(AdministrationStatusEnum.getByCode(requestBean.getAdministrationStatus()))) {
            return CheckTherapyEntryResponseBean.builder()
                    .status(ResponseCodesEnum.ADMINISTRATION_STATUS_CODE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.ADMINISTRATION_STATUS_CODE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.ADMINISTRATION_STATUS_CODE_NOT_FOUND.getDescription())
                    .build();
        }
        if (Boolean.FALSE.equals(therapyRecordDTO.get().getAdministrationNumber().equals(0)) &&
                therapyAdministrationRegistryDAO.retrieveByTherapyRecordWithState(requestBean.getTherapyRecordId(),
                                AdministrationStatusEnum.ADMINISTERED)
                        .size() == therapyRecordDTO.get().getAdministrationNumber()) {
            return CheckTherapyEntryResponseBean.builder()
                    .status(ResponseCodesEnum.ADMINISTRATION_NUMBER_REACHED.getStatus())
                    .responseCode(ResponseCodesEnum.ADMINISTRATION_NUMBER_REACHED.getErrorCode())
                    .responseMessage(ResponseCodesEnum.ADMINISTRATION_NUMBER_REACHED.getDescription())
                    .build();
        }
        Optional<TherapyAdministrationRegistryDTO> saved = therapyAdministrationRegistryDAO.register(TherapyAdministrationRegistryDTO.builder()
                .therapyRecordId(requestBean.getTherapyRecordId())
                .administrationInstant(Instant.now())
                .administratorId(requestBean.getAdministratorId())
                .administrationStatus(requestBean.getAdministrationStatus())
                .extraInfo(requestBean.getExtraInfo())
                .build());
        if (saved.isEmpty()) {
            return CheckTherapyEntryResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return CheckTherapyEntryResponseBean.builder()
                .checkId(saved.get().getId())
                .build();
    }
}
