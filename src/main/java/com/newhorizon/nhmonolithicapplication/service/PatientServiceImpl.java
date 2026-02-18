package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.RegisterPatientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrievePatientAnagraficaRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterPatientResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrievePatientAnagraficaResponseBean;
import com.newhorizon.nhmonolithicapplication.data.PatientDAO;
import com.newhorizon.nhmonolithicapplication.dto.PatientDTO;
import com.newhorizon.nhmonolithicapplication.enums.GenderTypeEnum;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PatientServiceImpl implements PatientService {
    private final PatientDAO patientDAO;

    @Override
    @SuppressWarnings("unchecked")
    public RetrievePatientAnagraficaResponseBean retrieveAnagrafica(RetrievePatientAnagraficaRequestBean requestBean) {
        Optional<PatientDTO> patientDTO = patientDAO.findPatientByFiscalCode(requestBean.getPatientFiscalCode());
        if (patientDTO.isEmpty()) {
            return RetrievePatientAnagraficaResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription())
                    .build();
        }
        return RetrievePatientAnagraficaResponseBean.builder()
                .patientName(patientDTO.get().getPatientName())
                .patientSurname(patientDTO.get().getPatientSurname())
                .dateOfBirth(patientDTO.get().getDateOfBirth())
                .patientFiscalCode(patientDTO.get().getPatientFiscalCode())
                .patientGender(patientDTO.get().getPatientGender())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RegisterPatientResponseBean registerNewPatient(RegisterPatientRequestBean requestBean) {
        if (patientDAO.findPatientByFiscalCode(requestBean.getPatientFiscalCode()).isPresent()) {
            return RegisterPatientResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_ALREADY_EXISTS.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_ALREADY_EXISTS.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_ALREADY_EXISTS.getDescription())
                    .build();
        }
        if (isNull(GenderTypeEnum.getGenderByCode(requestBean.getPatientGender()))) {
            return RegisterPatientResponseBean.builder()
                    .status(ResponseCodesEnum.GENDER_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.GENDER_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENDER_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<PatientDTO> patientDTO = patientDAO.createPatient(PatientDTO.builder()
                .patientName(requestBean.getPatientName())
                .patientSurname(requestBean.getPatientSurname())
                .dateOfBirth(requestBean.getDateOfBirth())
                .patientFiscalCode(requestBean.getPatientFiscalCode())
                .patientGender(requestBean.getPatientGender())
                .build());
        if (patientDTO.isEmpty()) {
            return RegisterPatientResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return RegisterPatientResponseBean.builder()
                .fiscalCode(patientDTO.get().getPatientFiscalCode())
                .build();
    }
}
