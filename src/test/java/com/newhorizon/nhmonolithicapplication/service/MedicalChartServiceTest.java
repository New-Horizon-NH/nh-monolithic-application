package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.NhMonolithicApplicationTest;
import com.newhorizon.nhmonolithicapplication.beans.request.GenerateMedicalChartRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterERDocumentRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterPatientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.GenerateMedicalChartResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterERDocumentResponseBean;
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
class MedicalChartServiceTest extends NhMonolithicApplicationTest {
    @Autowired
    MedicalChartServiceImpl medicalChartService;
    @Autowired
    PatientServiceImpl patientService;
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
    @DisplayName("Generate medical chart successfully")
    void generateMedicalChartOK() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        GenerateMedicalChartRequestBean requestBean = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        GenerateMedicalChartResponseBean serviceResponse = medicalChartService.generateMedicalChart(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    @DisplayName("Generate medical chart failing with patient not found")
    void generateMedicalChartKOPatientNotFound() {
        GenerateMedicalChartRequestBean requestBean = Instancio.of(GenerateMedicalChartRequestBean.class)
                .create();
        GenerateMedicalChartResponseBean serviceResponse = medicalChartService.generateMedicalChart(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.PATIENT_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Generate medical chart failing with chart already opened")
    void generateMedicalChartKOMedicalChartAlreadyOpened() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        GenerateMedicalChartRequestBean requestBean = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        GenerateMedicalChartResponseBean serviceResponse = medicalChartService.generateMedicalChart(requestBean);
        log.info(serviceResponse.toString());
        serviceResponse = medicalChartService.generateMedicalChart(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.MEDICAL_CHART_OPENED_DETECTED, serviceResponse);
    }

    @Test
    @DisplayName("Upload Emergency Room document successfully")
    @SneakyThrows
    void uploadERDocumentOK() {
        RegisterPatientRequestBean requestBeanPatient = Instancio.of(RegisterPatientRequestBean.class)
                .set(field(RegisterPatientRequestBean::getPatientName), "Antonio")
                .set(field(RegisterPatientRequestBean::getPatientSurname), "Russi")
                .set(field(RegisterPatientRequestBean::getDateOfBirth), LocalDate.of(2000, 10, 27))
                .set(field(RegisterPatientRequestBean::getPatientFiscalCode), "RSSNTN00R27L049N")
                .set(field(RegisterPatientRequestBean::getPatientGender), GenderTypeEnum.MALE.getGenderCode())
                .create();
        patientService.registerNewPatient(requestBeanPatient);
        GenerateMedicalChartRequestBean requestBeanChart = Instancio.of(GenerateMedicalChartRequestBean.class)
                .set(field(GenerateMedicalChartRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        String chartId = medicalChartService.generateMedicalChart(requestBeanChart).getMedicalChart();
        InputStream examResult = ExamServiceTest.class.getResourceAsStream("static/95b1cd2b-5aee-4aeb-989a-875fe5ac86ce.pdf");
        MultipartFile attachment = new MockMultipartFile(UUID.randomUUID().toString(),
                "95b1cd2b-5aee-4aeb-989a-875fe5ac86ce.pdf",
                "application/pdf",
                examResult);
        RegisterERDocumentRequestBean requestBean = Instancio.of(RegisterERDocumentRequestBean.class)
                .set(field(RegisterERDocumentRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .set(field(RegisterERDocumentRequestBean::getAttachment), attachment)
                .set(field(RegisterERDocumentRequestBean::getFileExtension), ".pdf")
                .create();
        RegisterERDocumentResponseBean serviceResponse = medicalChartService.uploadERDocument(requestBean);
        log.info(serviceResponse.toString());

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
        assertEquals(chartId, serviceResponse.getMedicalChartId());
    }
}