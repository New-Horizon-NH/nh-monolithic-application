package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.beans.request.AddResultToExamRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.PrescribePatientExamRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveExamResultsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnprescribePatientExamRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.AddResultToExamResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.PrescribePatientExamResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrieveExamResultsResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UnprescribePatientExamResponseBean;
import com.newhorizon.nhmonolithicapplication.data.ExamDAO;
import com.newhorizon.nhmonolithicapplication.data.ExamResultDAO;
import com.newhorizon.nhmonolithicapplication.data.ExamTypeDAO;
import com.newhorizon.nhmonolithicapplication.data.MedicalChartDAO;
import com.newhorizon.nhmonolithicapplication.data.PatientDAO;
import com.newhorizon.nhmonolithicapplication.dto.ExamDTO;
import com.newhorizon.nhmonolithicapplication.dto.ExamResultDTO;
import com.newhorizon.nhmonolithicapplication.dto.MedicalChartDTO;
import com.newhorizon.nhmonolithicapplication.dto.PatientDTO;
import com.newhorizon.nhmonolithicapplication.enums.ExamStatusEnum;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ExamServiceImpl implements ExamService {
    private final PatientDAO patientDAO;
    private final ExamTypeDAO examTypeDAO;
    private final ExamResultDAO examResultDAO;
    private final MedicalChartDAO medicalChartDAO;
    private final ExamDAO examDAO;
    private final BucketService bucketService;
    private final DocumentUploadConfigurationProperties documentUploadConfigurationProperties;

    @Override
    @SuppressWarnings("unchecked")
    public PrescribePatientExamResponseBean prescribeExamToPatient(PrescribePatientExamRequestBean requestBean) {
        if (patientDAO.findPatientByFiscalCode(requestBean.getPatientFiscalCode()).isEmpty()) {
            return PrescribePatientExamResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription())
                    .build();
        }
        if (examTypeDAO.findByCode(requestBean.getExamCode()).isEmpty()) {
            return PrescribePatientExamResponseBean.builder()
                    .status(ResponseCodesEnum.EXAM_CODE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.EXAM_CODE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.EXAM_CODE_NOT_FOUND.getDescription())
                    .build();
        }
        if (medicalChartDAO.retrieveOpenMedicalChart(requestBean.getPatientFiscalCode()).isEmpty()) {
            return PrescribePatientExamResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<ExamDTO> savedExam = examDAO.prescribeExam(requestBean.getExamCode(),
                requestBean.getPatientFiscalCode(),
                requestBean.getExamDate());
        if (savedExam.isEmpty()) {
            return PrescribePatientExamResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return PrescribePatientExamResponseBean.builder()
                .examCode(savedExam.get().getExamId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public UnprescribePatientExamResponseBean unprescribeExamToPatient(UnprescribePatientExamRequestBean requestBean) {
        log.info("Removing exam {} prescription",requestBean.getExamId());
        if (patientDAO.findPatientByFiscalCode(requestBean.getPatientFiscalCode()).isEmpty()) {
            log.error("Patient {} not found",requestBean.getPatientFiscalCode());
            return UnprescribePatientExamResponseBean.builder()
                    .status(ResponseCodesEnum.PATIENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PATIENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PATIENT_NOT_FOUND.getDescription())
                    .build();
        }
        if (medicalChartDAO.retrieveOpenMedicalChart(requestBean.getPatientFiscalCode()).isEmpty()) {
            return UnprescribePatientExamResponseBean.builder()
                    .status(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND.getDescription())
                    .build();
        }
        log.info("Patient {} exists in database", requestBean.getPatientFiscalCode());
        Optional<ExamDTO> examDTO = examDAO.findExamById(requestBean.getExamId());
        if (examDTO.isEmpty()) {
            log.error("Exam not found in database");
            return UnprescribePatientExamResponseBean.builder()
                    .status(ResponseCodesEnum.PRESCRIBED_EXAM_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PRESCRIBED_EXAM_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PRESCRIBED_EXAM_NOT_FOUND.getDescription())
                    .build();
        }
        log.info("Exam {} exists in database", examDTO.get().getExamId());
        if (Boolean.FALSE.equals(examDAO.isExamPrescribedToPatient(requestBean.getPatientFiscalCode(),
                requestBean.getExamId()))) {
            log.error("Exam {} is not prescribed to {}", examDTO.get().getExamId(), requestBean.getPatientFiscalCode());
            return UnprescribePatientExamResponseBean.builder()
                    .status(ResponseCodesEnum.EXAM_NOT_ASSIGNED_TO_GIVEN_PATIENT.getStatus())
                    .responseCode(ResponseCodesEnum.EXAM_NOT_ASSIGNED_TO_GIVEN_PATIENT.getErrorCode())
                    .responseMessage(ResponseCodesEnum.EXAM_NOT_ASSIGNED_TO_GIVEN_PATIENT.getDescription())
                    .build();

        }
        log.info("Exam {} match belonging to {}", examDTO.get().getExamId(), requestBean.getPatientFiscalCode());
        if (Boolean.FALSE.equals(ExamStatusEnum.CREATED.equals(ExamStatusEnum.getStatusByCode(examDTO.get().getExamStatus())))) {
            log.error("Exam {} cannot be removed", examDTO.get().getExamId());
            return UnprescribePatientExamResponseBean.builder()
                    .status(ResponseCodesEnum.EXAM_NOT_REMOVABLE.getStatus())
                    .responseCode(ResponseCodesEnum.EXAM_NOT_REMOVABLE.getErrorCode())
                    .responseMessage(ResponseCodesEnum.EXAM_NOT_REMOVABLE.getDescription())
                    .build();

        }
        log.info("Removing exam prescription");
        Optional<ExamDTO> unprescribedExam = examDAO.unprescribeExam(requestBean.getExamId(),
                requestBean.getPatientFiscalCode());
        if (unprescribedExam.isEmpty()) {
            log.error("Error removing exam prescription");
            return UnprescribePatientExamResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        log.info("Exam prescription removed");
        return UnprescribePatientExamResponseBean.builder()
                .examId(unprescribedExam.get().getExamId())
                .build();

    }

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public AddResultToExamResponseBean addResultToExam(AddResultToExamRequestBean requestBean) {
        if (examDAO.findExamById(requestBean.getExamId()).isEmpty()) {
            return AddResultToExamResponseBean.builder()
                    .status(ResponseCodesEnum.PRESCRIBED_EXAM_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PRESCRIBED_EXAM_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PRESCRIBED_EXAM_NOT_FOUND.getDescription())
                    .build();
        }
        PatientDTO patientDTO = patientDAO.retrievePatientByExamCode(requestBean.getExamId()).orElseThrow();
        MedicalChartDTO medicalChartDTO = medicalChartDAO.retrieveOpenMedicalChart(patientDTO.getPatientFiscalCode()).orElseThrow();
        String filename = UUID.randomUUID().toString();
        File temporaryFile = Files.createTempFile(filename, requestBean.getFileExtension()).toFile();
        requestBean.getAttachment().transferTo(temporaryFile);
        String filePath = MessageFormat.format(documentUploadConfigurationProperties.getExamResult(),
                patientDTO.getPatientFiscalCode(),
                medicalChartDTO.getMedicalChartId(),
                filename,
                requestBean.getFileExtension());
        temporaryFile.deleteOnExit();
        if (Boolean.FALSE.equals(bucketService.uploadFile(filePath,
                temporaryFile))) {
            return AddResultToExamResponseBean.builder()
                    .status(ResponseCodesEnum.ERROR_UPLOADING_EXAM_RESULT.getStatus())
                    .responseCode(ResponseCodesEnum.ERROR_UPLOADING_EXAM_RESULT.getErrorCode())
                    .responseMessage(ResponseCodesEnum.ERROR_UPLOADING_EXAM_RESULT.getDescription())
                    .build();
        }
        Optional<ExamResultDTO> savedResult = examResultDAO.saveExamResult(ExamResultDTO.builder()
                .examId(requestBean.getExamId())
                .uploadFilePath(filePath)
                .build());
        examDAO.updateStatus(requestBean.getExamId(),
                ExamStatusEnum.RESULTS_PUBLISHED.getExamStatusCode());
        if (savedResult.isEmpty()) {
            return AddResultToExamResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return AddResultToExamResponseBean.builder()
                .examId(requestBean.getExamId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RetrieveExamResultsResponseBean retrieveExamResults(RetrieveExamResultsRequestBean requestBean) {
        if (examDAO.findExamById(requestBean.getExamId()).isEmpty()) {
            return RetrieveExamResultsResponseBean.builder()
                    .status(ResponseCodesEnum.PRESCRIBED_EXAM_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.PRESCRIBED_EXAM_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.PRESCRIBED_EXAM_NOT_FOUND.getDescription())
                    .build();
        }
        List<File> results = examResultDAO.retrieveResultByExamId(requestBean.getExamId())
                .stream()
                .map(ExamResultDTO::getUploadFilePath)
                .map(bucketService::getFileObject)
                .toList();
        return RetrieveExamResultsResponseBean.builder()
                .results(results)
                .build();
    }
}
