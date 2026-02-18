package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.NhMonolithicApplicationTest;
import com.newhorizon.nhmonolithicapplication.beans.request.AddMedicalTeamMemberRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.AssignTreatmentToTherapyRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CheckTherapyEntryRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateDrugPackageRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateSUTRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.GenerateMedicalChartRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterDrugRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterPatientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.AssignTreatmentToTherapyResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CheckTherapyEntryResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateSUTResponseBean;
import com.newhorizon.nhmonolithicapplication.data.DrugRepository;
import com.newhorizon.nhmonolithicapplication.enums.AdministrationStatusEnum;
import com.newhorizon.nhmonolithicapplication.enums.AdministrationTypeEnum;
import com.newhorizon.nhmonolithicapplication.enums.GenderTypeEnum;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.instancio.Random;
import org.instancio.generator.Generator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;

import static org.instancio.Select.field;

@Slf4j
@Transactional
class TherapyServiceTest extends NhMonolithicApplicationTest {
    @Autowired
    TherapyServiceImpl therapyService;
    @Autowired
    PatientService patientService;
    @Autowired
    MedicalTeamServiceImpl medicalTeamService;
    @Autowired
    MedicalChartServiceImpl medicalChartService;
    @Autowired
    DrugServiceImpl drugService;
    @Autowired
    DrugRepository drugRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Create SUT successfully")
    void createSUTOK() {
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
        medicalChartService.generateMedicalChart(requestBeanChart);
        AddMedicalTeamMemberRequestBean requestBeanMedical = Instancio.of(AddMedicalTeamMemberRequestBean.class)
                .create();
        String medicalId = medicalTeamService.addMember(requestBeanMedical).getMemberId();
        CreateSUTRequestBean requestBean = Instancio.of(CreateSUTRequestBean.class)
                .set(field(CreateSUTRequestBean::getMedicalId), medicalId)
                .set(field(CreateSUTRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        CreateSUTResponseBean serviceResponse = therapyService.createSUT(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    void assignTreatmentToSUTOK() {
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
        medicalChartService.generateMedicalChart(requestBeanChart);
        AddMedicalTeamMemberRequestBean requestBeanMedical = Instancio.of(AddMedicalTeamMemberRequestBean.class)
                .create();
        String medicalId = medicalTeamService.addMember(requestBeanMedical).getMemberId();
        CreateSUTRequestBean requestBeanSut = Instancio.of(CreateSUTRequestBean.class)
                .set(field(CreateSUTRequestBean::getMedicalId), medicalId)
                .set(field(CreateSUTRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        String sutId = therapyService.createSUT(requestBeanSut).getSutId();
        RegisterDrugRequestBean requestBeanDrug = Instancio.of(RegisterDrugRequestBean.class)
                .generate(field(RegisterDrugRequestBean::getActiveIngredients), gen -> gen.collection().size(3))
                .create();
        drugService.createDrug(requestBeanDrug);
        CreateDrugPackageRequestBean requestBeanPackage = Instancio.of(CreateDrugPackageRequestBean.class)
                .set(field(CreateDrugPackageRequestBean::getDrugId), drugRepository.retrieveDrugByComplex(requestBeanDrug.getDrugName(),
                        requestBeanDrug.getPharmaceuticalCompany(),
                        requestBeanDrug.getPharmaceuticalForm(),
                        requestBeanDrug.getDosageFormDescription()).orElseThrow().getDrugId())
                .create();
        String packageId = drugService.createDrugPackage(requestBeanPackage).getPackageId();
        AssignTreatmentToTherapyRequestBean requestBean = Instancio.of(AssignTreatmentToTherapyRequestBean.class)
                .set(field(AssignTreatmentToTherapyRequestBean::getSutId), sutId)
                .set(field(AssignTreatmentToTherapyRequestBean::getAdministrationPackageId), packageId)
                .set(field(AssignTreatmentToTherapyRequestBean::getAdministrationNumber), 10)
                .set(field(AssignTreatmentToTherapyRequestBean::getAdministrationType), AdministrationTypeEnum.INTRAMUSCULAR.getAdministrationTypeCode())
                .set(field(AssignTreatmentToTherapyRequestBean::getMedicalAssigneeId), medicalId)
                .create();
        AssignTreatmentToTherapyResponseBean serviceResponse = therapyService.assignTreatmentToSUT(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    @DisplayName("Check treatment successfully")
    void checkTreatmentOK() {
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
        medicalChartService.generateMedicalChart(requestBeanChart);
        AddMedicalTeamMemberRequestBean requestBeanMedical = Instancio.of(AddMedicalTeamMemberRequestBean.class)
                .create();
        String medicalId = medicalTeamService.addMember(requestBeanMedical).getMemberId();
        CreateSUTRequestBean requestBeanSut = Instancio.of(CreateSUTRequestBean.class)
                .set(field(CreateSUTRequestBean::getMedicalId), medicalId)
                .set(field(CreateSUTRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        String sutId = therapyService.createSUT(requestBeanSut).getSutId();
        RegisterDrugRequestBean requestBeanDrug = Instancio.of(RegisterDrugRequestBean.class)
                .generate(field(RegisterDrugRequestBean::getActiveIngredients), gen -> gen.collection().size(3))
                .create();
        drugService.createDrug(requestBeanDrug);
        CreateDrugPackageRequestBean requestBeanPackage = Instancio.of(CreateDrugPackageRequestBean.class)
                .set(field(CreateDrugPackageRequestBean::getDrugId), drugRepository.retrieveDrugByComplex(requestBeanDrug.getDrugName(),
                        requestBeanDrug.getPharmaceuticalCompany(),
                        requestBeanDrug.getPharmaceuticalForm(),
                        requestBeanDrug.getDosageFormDescription()).orElseThrow().getDrugId())
                .create();
        String packageId = drugService.createDrugPackage(requestBeanPackage).getPackageId();
        AssignTreatmentToTherapyRequestBean requestBeanAssignment = Instancio.of(AssignTreatmentToTherapyRequestBean.class)
                .set(field(AssignTreatmentToTherapyRequestBean::getSutId), sutId)
                .set(field(AssignTreatmentToTherapyRequestBean::getAdministrationPackageId), packageId)
                .set(field(AssignTreatmentToTherapyRequestBean::getAdministrationNumber), 10)
                .set(field(AssignTreatmentToTherapyRequestBean::getAdministrationType), AdministrationTypeEnum.INTRAMUSCULAR.getAdministrationTypeCode())
                .set(field(AssignTreatmentToTherapyRequestBean::getMedicalAssigneeId), medicalId)
                .create();
        String therapyRecordId = therapyService.assignTreatmentToSUT(requestBeanAssignment).getAssignmentId();
        CheckTherapyEntryRequestBean requestBean = Instancio.of(CheckTherapyEntryRequestBean.class)
                .set(field(CheckTherapyEntryRequestBean::getTherapyRecordId), therapyRecordId)
                .set(field(CheckTherapyEntryRequestBean::getAdministratorId), medicalId)
                .set(field(CheckTherapyEntryRequestBean::getAdministrationStatus), AdministrationStatusEnum.ADMINISTERED.getAdministrationTypeCode())
                .create();
        CheckTherapyEntryResponseBean serviceResponse = therapyService.checkTreatement(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    @DisplayName("Check treatment failing with therapy not found")
    void checkTreatmentKOTherapyNotFound() {
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
        medicalChartService.generateMedicalChart(requestBeanChart);
        AddMedicalTeamMemberRequestBean requestBeanMedical = Instancio.of(AddMedicalTeamMemberRequestBean.class)
                .create();
        String medicalId = medicalTeamService.addMember(requestBeanMedical).getMemberId();
        CreateSUTRequestBean requestBeanSut = Instancio.of(CreateSUTRequestBean.class)
                .set(field(CreateSUTRequestBean::getMedicalId), medicalId)
                .set(field(CreateSUTRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        String sutId = therapyService.createSUT(requestBeanSut).getSutId();
        RegisterDrugRequestBean requestBeanDrug = Instancio.of(RegisterDrugRequestBean.class)
                .generate(field(RegisterDrugRequestBean::getActiveIngredients), gen -> gen.collection().size(3))
                .create();
        drugService.createDrug(requestBeanDrug);
        CreateDrugPackageRequestBean requestBeanPackage = Instancio.of(CreateDrugPackageRequestBean.class)
                .set(field(CreateDrugPackageRequestBean::getDrugId), drugRepository.retrieveDrugByComplex(requestBeanDrug.getDrugName(),
                        requestBeanDrug.getPharmaceuticalCompany(),
                        requestBeanDrug.getPharmaceuticalForm(),
                        requestBeanDrug.getDosageFormDescription()).orElseThrow().getDrugId())
                .create();
        String packageId = drugService.createDrugPackage(requestBeanPackage).getPackageId();
//        AssignTreatmentToTherapyRequestBean requestBeanAssignment = Instancio.of(AssignTreatmentToTherapyRequestBean.class)
//                .set(field(AssignTreatmentToTherapyRequestBean::getSutId), sutId)
//                .set(field(AssignTreatmentToTherapyRequestBean::getAdministrationPackageId), packageId)
//                .set(field(AssignTreatmentToTherapyRequestBean::getAdministrationNumber), 10)
//                .set(field(AssignTreatmentToTherapyRequestBean::getAdministrationType), AdministrationTypeEnum.INTRAMUSCULAR.getAdministrationTypeCode())
//                .set(field(AssignTreatmentToTherapyRequestBean::getMedicalAssigneeId), medicalId)
//                .create();
//        String therapyRecordId = therapyService.assignTreatmentToSUT(requestBeanAssignment).getAssignmentId();
        CheckTherapyEntryRequestBean requestBean = Instancio.of(CheckTherapyEntryRequestBean.class)
//                .set(field(CheckTherapyEntryRequestBean::getTherapyRecordId), therapyRecordId)
                .set(field(CheckTherapyEntryRequestBean::getAdministratorId), medicalId)
                .set(field(CheckTherapyEntryRequestBean::getAdministrationStatus), AdministrationStatusEnum.ADMINISTERED.getAdministrationTypeCode())
                .create();
        CheckTherapyEntryResponseBean serviceResponse = therapyService.checkTreatement(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.THERAPY_ENTRY_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Check treatment failing with medical not found")
    void checkTreatmentKOMedicalNotFound() {
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
        medicalChartService.generateMedicalChart(requestBeanChart);
        AddMedicalTeamMemberRequestBean requestBeanMedical = Instancio.of(AddMedicalTeamMemberRequestBean.class)
                .create();
        String medicalId = medicalTeamService.addMember(requestBeanMedical).getMemberId();
        CreateSUTRequestBean requestBeanSut = Instancio.of(CreateSUTRequestBean.class)
                .set(field(CreateSUTRequestBean::getMedicalId), medicalId)
                .set(field(CreateSUTRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        String sutId = therapyService.createSUT(requestBeanSut).getSutId();
        RegisterDrugRequestBean requestBeanDrug = Instancio.of(RegisterDrugRequestBean.class)
                .generate(field(RegisterDrugRequestBean::getActiveIngredients), gen -> gen.collection().size(3))
                .create();
        drugService.createDrug(requestBeanDrug);
        CreateDrugPackageRequestBean requestBeanPackage = Instancio.of(CreateDrugPackageRequestBean.class)
                .set(field(CreateDrugPackageRequestBean::getDrugId), drugRepository.retrieveDrugByComplex(requestBeanDrug.getDrugName(),
                        requestBeanDrug.getPharmaceuticalCompany(),
                        requestBeanDrug.getPharmaceuticalForm(),
                        requestBeanDrug.getDosageFormDescription()).orElseThrow().getDrugId())
                .create();
        String packageId = drugService.createDrugPackage(requestBeanPackage).getPackageId();
        AssignTreatmentToTherapyRequestBean requestBeanAssignment = Instancio.of(AssignTreatmentToTherapyRequestBean.class)
                .set(field(AssignTreatmentToTherapyRequestBean::getSutId), sutId)
                .set(field(AssignTreatmentToTherapyRequestBean::getAdministrationPackageId), packageId)
                .set(field(AssignTreatmentToTherapyRequestBean::getAdministrationNumber), 10)
                .set(field(AssignTreatmentToTherapyRequestBean::getAdministrationType), AdministrationTypeEnum.INTRAMUSCULAR.getAdministrationTypeCode())
                .set(field(AssignTreatmentToTherapyRequestBean::getMedicalAssigneeId), medicalId)
                .create();
        String therapyRecordId = therapyService.assignTreatmentToSUT(requestBeanAssignment).getAssignmentId();
        CheckTherapyEntryRequestBean requestBean = Instancio.of(CheckTherapyEntryRequestBean.class)
                .set(field(CheckTherapyEntryRequestBean::getTherapyRecordId), therapyRecordId)
//                .set(field(CheckTherapyEntryRequestBean::getAdministratorId), medicalId)
                .set(field(CheckTherapyEntryRequestBean::getAdministrationStatus), AdministrationStatusEnum.ADMINISTERED.getAdministrationTypeCode())
                .create();
        CheckTherapyEntryResponseBean serviceResponse = therapyService.checkTreatement(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.MEDICAL_MEMBER_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Check treatment successfully")
    void checkTreatment() {
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
        medicalChartService.generateMedicalChart(requestBeanChart);
        AddMedicalTeamMemberRequestBean requestBeanMedical = Instancio.of(AddMedicalTeamMemberRequestBean.class)
                .create();
        String medicalId = medicalTeamService.addMember(requestBeanMedical).getMemberId();
        CreateSUTRequestBean requestBeanSut = Instancio.of(CreateSUTRequestBean.class)
                .set(field(CreateSUTRequestBean::getMedicalId), medicalId)
                .set(field(CreateSUTRequestBean::getPatientFiscalCode), requestBeanPatient.getPatientFiscalCode())
                .create();
        String sutId = therapyService.createSUT(requestBeanSut).getSutId();
        RegisterDrugRequestBean requestBeanDrug = Instancio.of(RegisterDrugRequestBean.class)
                .generate(field(RegisterDrugRequestBean::getActiveIngredients), gen -> gen.collection().size(3))
                .create();
        drugService.createDrug(requestBeanDrug);
        CreateDrugPackageRequestBean requestBeanPackage = Instancio.of(CreateDrugPackageRequestBean.class)
                .set(field(CreateDrugPackageRequestBean::getDrugId), drugRepository.retrieveDrugByComplex(requestBeanDrug.getDrugName(),
                        requestBeanDrug.getPharmaceuticalCompany(),
                        requestBeanDrug.getPharmaceuticalForm(),
                        requestBeanDrug.getDosageFormDescription()).orElseThrow().getDrugId())
                .create();
        String packageId = drugService.createDrugPackage(requestBeanPackage).getPackageId();
        AssignTreatmentToTherapyRequestBean requestBeanAssignment = Instancio.of(AssignTreatmentToTherapyRequestBean.class)
                .set(field(AssignTreatmentToTherapyRequestBean::getSutId), sutId)
                .set(field(AssignTreatmentToTherapyRequestBean::getAdministrationPackageId), packageId)
                .set(field(AssignTreatmentToTherapyRequestBean::getAdministrationNumber), 10)
                .set(field(AssignTreatmentToTherapyRequestBean::getAdministrationType), AdministrationTypeEnum.INTRAMUSCULAR.getAdministrationTypeCode())
                .set(field(AssignTreatmentToTherapyRequestBean::getMedicalAssigneeId), medicalId)
                .create();
        String therapyRecordId = therapyService.assignTreatmentToSUT(requestBeanAssignment).getAssignmentId();
        CheckTherapyEntryRequestBean requestBean = Instancio.of(CheckTherapyEntryRequestBean.class)
                .set(field(CheckTherapyEntryRequestBean::getTherapyRecordId), therapyRecordId)
                .set(field(CheckTherapyEntryRequestBean::getAdministratorId), medicalId)
                .generate(field(CheckTherapyEntryRequestBean::getAdministrationStatus), new ExcludingValuesGenerator())
                .create();
        CheckTherapyEntryResponseBean serviceResponse = therapyService.checkTreatement(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.ADMINISTRATION_STATUS_CODE_NOT_FOUND, serviceResponse);
    }

    public static class ExcludingValuesGenerator implements Generator<Integer> {
        @Override
        public Integer generate(Random random) {
            Integer randomValue = random.intRange(1000, 10000);
            while (Arrays.stream(AdministrationStatusEnum.values())
                    .map(AdministrationStatusEnum::getAdministrationTypeCode)
                    .anyMatch(randomValue::equals)) {
                randomValue = random.intRange(1000, 10000);
            }
            return randomValue;
        }
    }
}