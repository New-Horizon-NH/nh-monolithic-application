package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.GenerateMedicalChartRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterERDocumentRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.GenerateMedicalChartResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterERDocumentResponseBean;
import com.newhorizon.nhmonolithicapplication.data.EmergencyRoomDocumentDAO;
import com.newhorizon.nhmonolithicapplication.data.MedicalChartDAO;
import com.newhorizon.nhmonolithicapplication.data.PatientDAO;
import com.newhorizon.nhmonolithicapplication.dto.EmergencyRoomDocumentDTO;
import com.newhorizon.nhmonolithicapplication.dto.MedicalChartDTO;
import com.newhorizon.nhmonolithicapplication.dto.PatientDTO;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import com.newhorizon.nhmonolithicapplication.properties.DocumentUploadConfigurationProperties;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MedicalChartServiceImpl implements MedicalChartService {
    private final PatientDAO patientDAO;
    private final MedicalChartDAO medicalChartDAO;
    private final EmergencyRoomDocumentDAO emergencyRoomDocumentDAO;
    private final BucketService bucketService;
    private final DocumentUploadConfigurationProperties documentUploadConfigurationProperties;

    @Override
    @SuppressWarnings("unchecked")
    public GenerateMedicalChartResponseBean generateMedicalChart(GenerateMedicalChartRequestBean requestBean) {
        if (patientDAO.findPatientByFiscalCode(requestBean.getPatientFiscalCode()).isEmpty()) {
            return GenerateMedicalChartResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription())
                    .build();
        }
        if (medicalChartDAO.retrieveOpenMedicalChart(requestBean.getPatientFiscalCode()).isPresent()) {
            return GenerateMedicalChartResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_CHART_OPENED_DETECTED.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_CHART_OPENED_DETECTED.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_CHART_OPENED_DETECTED.getDescription())
                    .build();
        }
        Optional<MedicalChartDTO> savedChart = medicalChartDAO.openMedicalChart(requestBean.getPatientFiscalCode());
        if (savedChart.isEmpty()) {
            return GenerateMedicalChartResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return GenerateMedicalChartResponseBean.builder()
                .medicalChart(savedChart.get().getMedicalChartId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public RegisterERDocumentResponseBean uploadERDocument(RegisterERDocumentRequestBean requestBean) {
        Optional<PatientDTO> patientDTO = patientDAO.findPatientByFiscalCode(requestBean.getPatientFiscalCode());
        if (patientDTO.isEmpty()) {
            return RegisterERDocumentResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<MedicalChartDTO> medicalChartDTO = medicalChartDAO.retrieveOpenMedicalChart(requestBean.getPatientFiscalCode());
        if (medicalChartDTO.isEmpty()) {
            return RegisterERDocumentResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getDescription())
                    .build();
        }

        String filename = UUID.randomUUID().toString();
        File temporaryFile = Files.createTempFile(filename, requestBean.getFileExtension()).toFile();
        requestBean.getAttachment().transferTo(temporaryFile);
        String filePath = MessageFormat.format(documentUploadConfigurationProperties.getEmergencyRoomDocument(),
                patientDTO.get().getPatientFiscalCode(),
                medicalChartDTO.get().getMedicalChartId(),
                filename,
                requestBean.getFileExtension());
        temporaryFile.deleteOnExit();
        if (Boolean.FALSE.equals(bucketService.uploadFile(filePath,
                temporaryFile))) {
            return RegisterERDocumentResponseBean.builder()
                    .status(ResponseCodesEnum.ERROR_UPLOADING_ER_DOCUMENT.getStatus())
                    .responseCode(ResponseCodesEnum.ERROR_UPLOADING_ER_DOCUMENT.getErrorCode())
                    .responseMessage(ResponseCodesEnum.ERROR_UPLOADING_ER_DOCUMENT.getDescription())
                    .build();
        }
        Optional<EmergencyRoomDocumentDTO> savedResult = emergencyRoomDocumentDAO.saveDocument(EmergencyRoomDocumentDTO.builder()
                .medicalChartId(medicalChartDTO.get().getMedicalChartId())
                .uploadFilePath(filePath)
                .build());
        if (savedResult.isEmpty()) {
            return RegisterERDocumentResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return RegisterERDocumentResponseBean.builder()
                .medicalChartId(savedResult.get().getMedicalChartId())
                .build();
    }
}
