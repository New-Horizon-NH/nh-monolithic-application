package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.NhMonolithicApplicationTest;
import com.newhorizon.nhmonolithicapplication.beans.request.AddResultToExamRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateExamTypeRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.GenerateMedicalChartRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.PrescribePatientExamRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterPatientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RetrieveExamResultsRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UnprescribePatientExamRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.AddResultToExamResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.PrescribePatientExamResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RetrieveExamResultsResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.UnprescribePatientExamResponseBean;
import com.newhorizon.nhmonolithicapplication.enums.GenderTypeEnum;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import io.minio.BucketExistsArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.SetBucketVersioningArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.VersioningConfiguration;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Transactional
class ExamServiceTest extends NhMonolithicApplicationTest {
    @Autowired
    ExamServiceImpl examService;
    @Autowired
    PatientServiceImpl patientService;
    @Autowired
    ExamInventoryServiceImpl examInventoryService;
    @Autowired
    MedicalChartServiceImpl medicalChartService;
    @Autowired
    MinioClient minioClient;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        if (!minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket("patient")
                .build())) {

            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket("patient")
                    .build());
            minioClient.setBucketVersioning(
                    SetBucketVersioningArgs.builder()
                            .bucket("patient")
                            .config(new VersioningConfiguration(VersioningConfiguration.Status.ENABLED,
                                    null))
                            .build());
        }
    }

    @AfterEach
    @SneakyThrows
    void tearDown() {
        AtomicReference<String> currentBucket = new AtomicReference<>();
        Stream.of("patient")
                .flatMap(bucket -> {
                    currentBucket.set(bucket);
                    return StreamSupport.stream(minioClient.listObjects(ListObjectsArgs.builder()
                            .bucket(bucket)
                            .build()).spliterator(), false
                    );
                })
                .map(itemResult -> {
                    try {
                        return itemResult.get().objectName();
                    } catch (ErrorResponseException | InsufficientDataException | InternalException |
                             InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                             ServerException | XmlParserException e) {
                        throw new RuntimeException(e);
                    }
                })
                .forEach(deleteObject -> {
                    try {
                        minioClient.removeObject(RemoveObjectArgs.builder()
                                .bucket(currentBucket.get())
                                .object(deleteObject)
                                .build());
                    } catch (ErrorResponseException | InsufficientDataException | InternalException |
                             InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                             ServerException | XmlParserException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Test
    @DisplayName("Prescribe exam to patient successfully")
    void prescribeExamToPatientOK() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        CreateExamTypeRequestBean requestBeanExamType = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        examInventoryService.createNewExamType(requestBeanExamType);
        GenerateMedicalChartRequestBean requestBeanChart = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        medicalChartService.generateMedicalChart(requestBeanChart);
        PrescribePatientExamRequestBean requestBean = Instancio.of(PrescribePatientExamRequestBean.class)
                .set(field(PrescribePatientExamRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(PrescribePatientExamRequestBean::getExamCode), requestBeanExamType.getExamCode())
                .create();
        PrescribePatientExamResponseBean serviceResponse = examService.prescribeExamToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    @DisplayName("Prescribe exam to patient failing with patient not found")
    void prescribeExamToPatientKOPatientNotFound() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        CreateExamTypeRequestBean requestBeanExamType = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        examInventoryService.createNewExamType(requestBeanExamType);
        GenerateMedicalChartRequestBean requestBeanChart = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        medicalChartService.generateMedicalChart(requestBeanChart);
        PrescribePatientExamRequestBean requestBean = Instancio.of(PrescribePatientExamRequestBean.class)
                .set(field(PrescribePatientExamRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(PrescribePatientExamRequestBean::getExamCode), requestBeanExamType.getExamCode())
                .create();
        PrescribePatientExamResponseBean serviceResponse = examService.prescribeExamToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.PATIENT_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Prescribe exam to patient failing with exam type not found")
    void prescribeExamToPatientKOExamTypeNotFound() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        CreateExamTypeRequestBean requestBeanExamType = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        GenerateMedicalChartRequestBean requestBeanChart = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        medicalChartService.generateMedicalChart(requestBeanChart);
        PrescribePatientExamRequestBean requestBean = Instancio.of(PrescribePatientExamRequestBean.class)
                .set(field(PrescribePatientExamRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(PrescribePatientExamRequestBean::getExamCode), requestBeanExamType.getExamCode())
                .create();
        PrescribePatientExamResponseBean serviceResponse = examService.prescribeExamToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.EXAM_CODE_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Prescribe exam to patient failing with no open medical chart")
    void prescribeExamToPatientKOMedicalChartNotFound() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        CreateExamTypeRequestBean requestBeanExamType = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        examInventoryService.createNewExamType(requestBeanExamType);
        PrescribePatientExamRequestBean requestBean = Instancio.of(PrescribePatientExamRequestBean.class)
                .set(field(PrescribePatientExamRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(PrescribePatientExamRequestBean::getExamCode), requestBeanExamType.getExamCode())
                .create();
        PrescribePatientExamResponseBean serviceResponse = examService.prescribeExamToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.MEDICAL_CHART_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Remove exam to patient successfully")
    void unprescribeExamToPatientOK() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        CreateExamTypeRequestBean requestBeanExamType = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        examInventoryService.createNewExamType(requestBeanExamType);
        GenerateMedicalChartRequestBean requestBeanChart = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        medicalChartService.generateMedicalChart(requestBeanChart);
        PrescribePatientExamRequestBean requestBeanExam = Instancio.of(PrescribePatientExamRequestBean.class)
                .set(field(PrescribePatientExamRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(PrescribePatientExamRequestBean::getExamCode), requestBeanExamType.getExamCode())
                .create();
        String examId = examService.prescribeExamToPatient(requestBeanExam).getExamCode();
        UnprescribePatientExamRequestBean requestBean = Instancio.of(UnprescribePatientExamRequestBean.class)
                .set(field(UnprescribePatientExamRequestBean::getExamId), examId)
                .set(field(UnprescribePatientExamRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        UnprescribePatientExamResponseBean serviceResponse = examService.unprescribeExamToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    @DisplayName("Remove exam to patient failing with patient not found")
    void unprescribeExamToPatientKOPatientNotFound() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        CreateExamTypeRequestBean requestBeanExamType = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        examInventoryService.createNewExamType(requestBeanExamType);
        GenerateMedicalChartRequestBean requestBeanChart = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        medicalChartService.generateMedicalChart(requestBeanChart);
        PrescribePatientExamRequestBean requestBeanExam = Instancio.of(PrescribePatientExamRequestBean.class)
                .set(field(PrescribePatientExamRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(PrescribePatientExamRequestBean::getExamCode), requestBeanExamType.getExamCode())
                .create();
        String examId = examService.prescribeExamToPatient(requestBeanExam).getExamCode();
        UnprescribePatientExamRequestBean requestBean = Instancio.of(UnprescribePatientExamRequestBean.class)
                .set(field(UnprescribePatientExamRequestBean::getExamId), examId)
                .set(field(UnprescribePatientExamRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        UnprescribePatientExamResponseBean serviceResponse = examService.unprescribeExamToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.PATIENT_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Remove exam to patient failing with exam not found")
    void unprescribeExamToPatientKOExamNotFound() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        CreateExamTypeRequestBean requestBeanExamType = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        examInventoryService.createNewExamType(requestBeanExamType);
        GenerateMedicalChartRequestBean requestBeanChart = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        medicalChartService.generateMedicalChart(requestBeanChart);
        UnprescribePatientExamRequestBean requestBean = Instancio.of(UnprescribePatientExamRequestBean.class)
                .set(field(UnprescribePatientExamRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        UnprescribePatientExamResponseBean serviceResponse = examService.unprescribeExamToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.PRESCRIBED_EXAM_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Remove exam to patient failing with exam not assigned to given patient")
    void unprescribeExamToPatientKOWrongPatientAssignment() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        RegisterPatientRequestBean requestBeanPatientSecond = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Elenoire Francesca")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Tedesco")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2002, 8, 2))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "TDSLRF02M42L049Q")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.FEMALE.getGenderCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        patientService.registerNewPatient(requestBeanPatientSecond);
        CreateExamTypeRequestBean requestBeanExamType = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        examInventoryService.createNewExamType(requestBeanExamType);
        GenerateMedicalChartRequestBean requestBeanChart = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        medicalChartService.generateMedicalChart(requestBeanChart);
        requestBeanChart = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatientSecond.getPatientFiscalCode())
                .create();
        medicalChartService.generateMedicalChart(requestBeanChart);
        PrescribePatientExamRequestBean requestBeanExam = Instancio.of(PrescribePatientExamRequestBean.class)
                .set(field(PrescribePatientExamRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(PrescribePatientExamRequestBean::getExamCode), requestBeanExamType.getExamCode())
                .create();
        String examId = examService.prescribeExamToPatient(requestBeanExam).getExamCode();

        UnprescribePatientExamRequestBean requestBean = Instancio.of(UnprescribePatientExamRequestBean.class)
                .set(field(UnprescribePatientExamRequestBean::getExamId), examId)
                .set(field(UnprescribePatientExamRequestBean::getPatientFiscalCode), requestBeanPatientSecond.getPatientFiscalCode())
                .create();
        UnprescribePatientExamResponseBean serviceResponse = examService.unprescribeExamToPatient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.EXAM_NOT_ASSIGNED_TO_GIVEN_PATIENT, serviceResponse);
    }

    @Test
    @DisplayName("Add exam result successfully")
    @SneakyThrows
    void addResultToExamOK() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        CreateExamTypeRequestBean requestBeanExamType = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        examInventoryService.createNewExamType(requestBeanExamType);
        GenerateMedicalChartRequestBean requestBeanChart = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        medicalChartService.generateMedicalChart(requestBeanChart);
        PrescribePatientExamRequestBean requestBeanExam = Instancio.of(PrescribePatientExamRequestBean.class)
                .set(field(PrescribePatientExamRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(PrescribePatientExamRequestBean::getExamCode), requestBeanExamType.getExamCode())
                .create();
        String examId = examService.prescribeExamToPatient(requestBeanExam).getExamCode();

        InputStream examResult = ExamServiceTest.class.getResourceAsStream("static/95b1cd2b-5aee-4aeb-989a-875fe5ac86ce.pdf");
        MultipartFile attachment = new MockMultipartFile(UUID.randomUUID().toString(),
                "95b1cd2b-5aee-4aeb-989a-875fe5ac86ce.pdf",
                "application/pdf",
                examResult);
        AddResultToExamRequestBean requestBean = Instancio.of(AddResultToExamRequestBean.class)
                .set(field(AddResultToExamRequestBean::getExamId), examId)
                .set(field(AddResultToExamRequestBean::getAttachment), attachment)
                .set(field(AddResultToExamRequestBean::getFileExtension), ".pdf")
                .create();
        AddResultToExamResponseBean serviceResponse = examService.addResultToExam(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    @DisplayName("Add exam result failing with exam not found")
    @SneakyThrows
    void addResultToExamKOExamNotFound() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        CreateExamTypeRequestBean requestBeanExamType = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        examInventoryService.createNewExamType(requestBeanExamType);
        GenerateMedicalChartRequestBean requestBeanChart = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        medicalChartService.generateMedicalChart(requestBeanChart);
        PrescribePatientExamRequestBean requestBeanExam = Instancio.of(PrescribePatientExamRequestBean.class)
                .set(field(PrescribePatientExamRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(PrescribePatientExamRequestBean::getExamCode), requestBeanExamType.getExamCode())
                .create();

        InputStream examResult = ExamServiceTest.class.getResourceAsStream("static/95b1cd2b-5aee-4aeb-989a-875fe5ac86ce.pdf");
        MultipartFile attachment = new MockMultipartFile(UUID.randomUUID().toString(),
                "95b1cd2b-5aee-4aeb-989a-875fe5ac86ce.pdf",
                "application/pdf",
                examResult);
        AddResultToExamRequestBean requestBean = Instancio.of(AddResultToExamRequestBean.class)
                .set(field(AddResultToExamRequestBean::getAttachment), attachment)
                .set(field(AddResultToExamRequestBean::getFileExtension), ".pdf")
                .create();
        AddResultToExamResponseBean serviceResponse = examService.addResultToExam(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.PRESCRIBED_EXAM_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Retrieve exam result successfully")
    @SneakyThrows
    void retrieveExamResultsOK() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        CreateExamTypeRequestBean requestBeanExamType = Instancio.of(CreateExamTypeRequestBean.class)
                .create();
        examInventoryService.createNewExamType(requestBeanExamType);
        GenerateMedicalChartRequestBean requestBeanChart = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        medicalChartService.generateMedicalChart(requestBeanChart);
        PrescribePatientExamRequestBean requestBeanExam = Instancio.of(PrescribePatientExamRequestBean.class)
                .set(field(PrescribePatientExamRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(PrescribePatientExamRequestBean::getExamCode), requestBeanExamType.getExamCode())
                .create();
        String examId = examService.prescribeExamToPatient(requestBeanExam).getExamCode();

        InputStream examResult = ExamServiceTest.class.getResourceAsStream("static/95b1cd2b-5aee-4aeb-989a-875fe5ac86ce.pdf");
        MultipartFile attachment = new MockMultipartFile(UUID.randomUUID().toString(),
                "95b1cd2b-5aee-4aeb-989a-875fe5ac86ce.pdf",
                "application/pdf",
                examResult);
        AddResultToExamRequestBean requestBeanUpload = Instancio.of(AddResultToExamRequestBean.class)
                .set(field(AddResultToExamRequestBean::getExamId), examId)
                .set(field(AddResultToExamRequestBean::getAttachment), attachment)
                .set(field(AddResultToExamRequestBean::getFileExtension), ".pdf")
                .create();
        examService.addResultToExam(requestBeanUpload);
        RetrieveExamResultsRequestBean requestBean = Instancio.of(RetrieveExamResultsRequestBean.class)
                .set(field(RetrieveExamResultsRequestBean::getExamId), examId)
                .create();
        RetrieveExamResultsResponseBean serviceResponse = examService.retrieveExamResults(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
        assertEquals(1, serviceResponse.getResults().size());
    }

    @Test
    @DisplayName("Retrieve exam result failing with no exam found")
    void retrieveExamResultsKOExamNotFound(){
        RetrieveExamResultsRequestBean requestBean = Instancio.of(RetrieveExamResultsRequestBean.class)
                .create();
        RetrieveExamResultsResponseBean serviceResponse = examService.retrieveExamResults(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.PRESCRIBED_EXAM_NOT_FOUND, serviceResponse);
    }
}