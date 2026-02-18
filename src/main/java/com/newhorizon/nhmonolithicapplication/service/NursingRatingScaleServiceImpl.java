package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.RegisterScaleRecordRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveScaleListRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterScaleRecordResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrieveScaleListResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.ScaleResponse;
import com.newhorizon.nhmonolithicapplication.data.MedicalChartDAO;
import com.newhorizon.nhmonolithicapplication.data.NursingRatingScaleRegistryDAO;
import com.newhorizon.nhmonolithicapplication.data.PatientDAO;
import com.newhorizon.nhmonolithicapplication.dto.MedicalChartDTO;
import com.newhorizon.nhmonolithicapplication.dto.NursingRatingScaleDTO;
import com.newhorizon.nhmonolithicapplication.enums.NursingRatingScaleEnum;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NursingRatingScaleServiceImpl implements NursingRatingScaleService {
    private final PatientDAO patientDAO;
    private final NursingRatingScaleRegistryDAO nursingRatingScaleRegistryDAO;
    private final MedicalChartDAO medicalChartDAO;

    @Override
    @SuppressWarnings("unchecked")
    public RegisterScaleRecordResponseBean createRecord(RegisterScaleRecordRequestBean requestBean) {
        if (patientDAO.findPatientByFiscalCode(requestBean.getPatientFiscalCode()).isEmpty()) {
            return RegisterScaleRecordResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription())
                    .build();
        }
        if (isNull(NursingRatingScaleEnum.getScaleByCode(requestBean.getScaleCode()))) {
            return RegisterScaleRecordResponseBean.builder()
                    .status(ResponseCodesEnum.NURSING_RATING_SCALE_CODE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.NURSING_RATING_SCALE_CODE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.NURSING_RATING_SCALE_CODE_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<MedicalChartDTO> medicalChartDTO = medicalChartDAO.retrieveOpenMedicalChart(requestBean.getPatientFiscalCode());
        if (medicalChartDTO.isEmpty()) {
            return RegisterScaleRecordResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<NursingRatingScaleDTO> saved = nursingRatingScaleRegistryDAO.createRecord(NursingRatingScaleDTO.builder()
                .medicalChartId(medicalChartDTO.get().getMedicalChartId())
                .scaleCode(requestBean.getScaleCode())
                .value(requestBean.getValue())
                .build());
        if (saved.isEmpty()) {
            return RegisterScaleRecordResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return RegisterScaleRecordResponseBean.builder()
                .recordId(saved.get().getId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RetrieveScaleListResponseBean retrieveMedicalChartScaleList(RetrieveScaleListRequestBean requestBean) {
        Optional<MedicalChartDTO> medicalChartDTO = medicalChartDAO.retrieveMedicalChart(requestBean.getMedicalChartId());
        if (medicalChartDTO.isEmpty()) {
            return RetrieveScaleListResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getDescription())
                    .build();
        }
        List<NursingRatingScaleDTO> scales = nursingRatingScaleRegistryDAO.retrieveAllByMedicalChart(requestBean.getMedicalChartId());
        return RetrieveScaleListResponseBean.builder()
                .scales(scales.stream()
                        .map(scale -> ScaleResponse.builder()
                                .id(scale.getId())
                                .scaleCode(scale.getScaleCode())
                                .scaleName(NursingRatingScaleEnum.getScaleByCode(scale.getScaleCode()).getNursingScaleName())
                                .scaleValue(scale.getValue())
                                .build())
                        .toList())
                .build();
    }
}
