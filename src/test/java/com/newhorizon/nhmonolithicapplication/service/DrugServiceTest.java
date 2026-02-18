package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.NhMonolithicApplicationTest;
import com.newhorizon.nhmonolithicapplication.beans.request.AddMedicalTeamMemberRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateActiveIngredientRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateDrugActiveIngredientAssociationRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.CreateDrugPackageRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.RegisterDrugRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.UpdateDrugFornitureRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.request.WithdrawDrugRequestBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateActiveIngredientResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateDrugActiveIngredientAssociationResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.CreateDrugPackageResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.RegisterDrugResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.WithdrawDrugResponseBean;
import com.newhorizon.nhmonolithicapplication.data.DrugRepository;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
@Transactional
class DrugServiceTest extends NhMonolithicApplicationTest {
    @Autowired
    DrugServiceImpl drugService;
    @Autowired
    DrugRepository drugRepository;
    @Autowired
    MedicalTeamServiceImpl medicalTeamService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Create active ingredient successfully")
    void createActiveIngredientOK() {
        CreateActiveIngredientRequestBean requestBean = Instancio.of(CreateActiveIngredientRequestBean.class)
                .create();
        CreateActiveIngredientResponseBean serviceResponse = drugService.createActiveIngredient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
        assertEquals(requestBean.getActiveIngredient(), serviceResponse.getActiveIngredient());
    }

    @Test
    @DisplayName("Create active ingredient failing with active ingredient already exists")
    void createActiveIngredientKOAlreadyExists() {
        CreateActiveIngredientRequestBean requestBean = Instancio.of(CreateActiveIngredientRequestBean.class)
                .create();
        CreateActiveIngredientResponseBean serviceResponse = drugService.createActiveIngredient(requestBean);
        log.info("Create first time, {}", serviceResponse);
        serviceResponse = drugService.createActiveIngredient(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.ACTIVE_INGREDIENT_ALREADY_EXISTS, serviceResponse);
    }

    @Test
    @DisplayName("Create drug successfully")
    void createDrugOK() {
        RegisterDrugRequestBean requestBean = Instancio.of(RegisterDrugRequestBean.class)
                .generate(field(RegisterDrugRequestBean::getActiveIngredients), gen -> gen.collection().size(3))
                .create();
        RegisterDrugResponseBean serviceResponse = drugService.createDrug(requestBean);
        log.info(serviceResponse.toString());

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
        assertEquals(requestBean.getActiveIngredients().size(), serviceResponse.getActiveIngredientsAssociation().size());
    }

    @Test
    @DisplayName("Create drug failing with drug already exists")
    void createDrugKOAlreadyExists() {
        RegisterDrugRequestBean requestBean = Instancio.of(RegisterDrugRequestBean.class)
                .generate(field(RegisterDrugRequestBean::getActiveIngredients), gen -> gen.collection().size(3))
                .create();
        RegisterDrugResponseBean serviceResponse = drugService.createDrug(requestBean);
        log.info(serviceResponse.toString());
        serviceResponse = drugService.createDrug(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.DRUG_ALREADY_EXISTS, serviceResponse);
    }

    @Test
    @DisplayName("Create drug package successfully")
    void createDrugPackageOK() {
        RegisterDrugRequestBean requestBeanDrug = Instancio.of(RegisterDrugRequestBean.class)
                .generate(field(RegisterDrugRequestBean::getActiveIngredients), gen -> gen.collection().size(3))
                .create();
        drugService.createDrug(requestBeanDrug);
        CreateDrugPackageRequestBean requestBean = Instancio.of(CreateDrugPackageRequestBean.class)
                .set(field(CreateDrugPackageRequestBean::getDrugId), drugRepository.retrieveDrugByComplex(requestBeanDrug.getDrugName(),
                        requestBeanDrug.getPharmaceuticalCompany(),
                        requestBeanDrug.getPharmaceuticalForm(),
                        requestBeanDrug.getDosageFormDescription()).orElseThrow().getDrugId())
                .create();
        CreateDrugPackageResponseBean serviceResponse = drugService.createDrugPackage(requestBean);
        log.info(serviceResponse.toString());

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
        assertEquals(requestBean.getPackageId(), serviceResponse.getPackageId());
    }

    @Test
    @DisplayName("Create drug package failing with drug not found")
    void createDrugPackageKODrugNotFound() {
        CreateDrugPackageRequestBean requestBean = Instancio.of(CreateDrugPackageRequestBean.class)
                .create();
        CreateDrugPackageResponseBean serviceResponse = drugService.createDrugPackage(requestBean);
        log.info(serviceResponse.toString());

        assertResponseCodeEnum(ResponseCodesEnum.DRUG_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Create drug package failing with drug package already exists")
    void createDrugPackageKOPackageAlreadyExists() {
        RegisterDrugRequestBean requestBeanDrug = Instancio.of(RegisterDrugRequestBean.class)
                .generate(field(RegisterDrugRequestBean::getActiveIngredients), gen -> gen.collection().size(3))
                .create();
        drugService.createDrug(requestBeanDrug);
        CreateDrugPackageRequestBean requestBean = Instancio.of(CreateDrugPackageRequestBean.class)
                .set(field(CreateDrugPackageRequestBean::getDrugId), drugRepository.retrieveDrugByComplex(requestBeanDrug.getDrugName(),
                        requestBeanDrug.getPharmaceuticalCompany(),
                        requestBeanDrug.getPharmaceuticalForm(),
                        requestBeanDrug.getDosageFormDescription()).orElseThrow().getDrugId())
                .create();
        CreateDrugPackageResponseBean serviceResponse = drugService.createDrugPackage(requestBean);
        log.info(serviceResponse.toString());
        serviceResponse = drugService.createDrugPackage(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.DRUG_PACKAGE_ALREADY_EXISTS, serviceResponse);
    }

    @Test
    @DisplayName("Withdraw drug package successfully")
    void withdrawDrugPackageOK() {
        AddMedicalTeamMemberRequestBean requestBeanMedical = Instancio.of(AddMedicalTeamMemberRequestBean.class)
                .set(field(AddMedicalTeamMemberRequestBean::getName), "Antonio")
                .set(field(AddMedicalTeamMemberRequestBean::getSurname), "Russi")
                .set(field(AddMedicalTeamMemberRequestBean::getFiscalCode), "RSSNTN00R27L049N")
                .create();
        String memberId = medicalTeamService.addMember(requestBeanMedical).getMemberId();
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
        drugService.createDrugPackage(requestBeanPackage);
        UpdateDrugFornitureRequestBean requestBeanForniture = Instancio.of(UpdateDrugFornitureRequestBean.class)
                .set(field(UpdateDrugFornitureRequestBean::getPackageId), requestBeanPackage.getPackageId())
                .set(field(UpdateDrugFornitureRequestBean::getQuantity), 10L)
                .create();
        drugService.updateDrugForniture(requestBeanForniture);
        WithdrawDrugRequestBean requestBean = Instancio.of(WithdrawDrugRequestBean.class)
                .set(field(WithdrawDrugRequestBean::getPackageId), requestBeanPackage.getPackageId())
                .set(field(WithdrawDrugRequestBean::getQuantity), 2L)
                .set(field(WithdrawDrugRequestBean::getUserId), memberId)
                .create();
        WithdrawDrugResponseBean serviceResponse = drugService.withdrawDrugPackage(requestBean);
        log.info(serviceResponse.toString());

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
        assertEquals(requestBeanPackage.getPackageId(), serviceResponse.getPackageId());
        assertEquals(2L, serviceResponse.getQuantity());
    }

    @Test
    @DisplayName("Withdraw drug package failing with package not found")
    void withdrawDrugPackageKOPackageNotFound() {
        AddMedicalTeamMemberRequestBean requestBeanMedical = Instancio.of(AddMedicalTeamMemberRequestBean.class)
                .set(field(AddMedicalTeamMemberRequestBean::getName), "Antonio")
                .set(field(AddMedicalTeamMemberRequestBean::getSurname), "Russi")
                .set(field(AddMedicalTeamMemberRequestBean::getFiscalCode), "RSSNTN00R27L049N")
                .create();
        String memberId = medicalTeamService.addMember(requestBeanMedical).getMemberId();
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
        UpdateDrugFornitureRequestBean requestBeanForniture = Instancio.of(UpdateDrugFornitureRequestBean.class)
                .set(field(UpdateDrugFornitureRequestBean::getPackageId), requestBeanPackage.getPackageId())
                .set(field(UpdateDrugFornitureRequestBean::getQuantity), 10L)
                .create();
        drugService.updateDrugForniture(requestBeanForniture);
        WithdrawDrugRequestBean requestBean = Instancio.of(WithdrawDrugRequestBean.class)
                .set(field(WithdrawDrugRequestBean::getPackageId), requestBeanPackage.getPackageId())
                .set(field(WithdrawDrugRequestBean::getQuantity), 2L)
                .set(field(WithdrawDrugRequestBean::getUserId), memberId)
                .create();
        WithdrawDrugResponseBean serviceResponse = drugService.withdrawDrugPackage(requestBean);
        log.info(serviceResponse.toString());

        assertResponseCodeEnum(ResponseCodesEnum.DRUG_PACKAGE_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Withdraw drug package failing with quantity not available")
    void withdrawDrugPackageKOQuantityNotAvailable() {
        AddMedicalTeamMemberRequestBean requestBeanMedical = Instancio.of(AddMedicalTeamMemberRequestBean.class)
                .set(field(AddMedicalTeamMemberRequestBean::getName), "Antonio")
                .set(field(AddMedicalTeamMemberRequestBean::getSurname), "Russi")
                .set(field(AddMedicalTeamMemberRequestBean::getFiscalCode), "RSSNTN00R27L049N")
                .create();
        String memberId = medicalTeamService.addMember(requestBeanMedical).getMemberId();
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
        drugService.createDrugPackage(requestBeanPackage);
        WithdrawDrugRequestBean requestBean = Instancio.of(WithdrawDrugRequestBean.class)
                .set(field(WithdrawDrugRequestBean::getPackageId), requestBeanPackage.getPackageId())
                .set(field(WithdrawDrugRequestBean::getQuantity), 2L)
                .set(field(WithdrawDrugRequestBean::getUserId), memberId)
                .create();
        WithdrawDrugResponseBean serviceResponse = drugService.withdrawDrugPackage(requestBean);
        log.info(serviceResponse.toString());

        assertResponseCodeEnum(ResponseCodesEnum.DRUG_QUANTITY_NOT_AVAILABLE, serviceResponse);
    }

    @Test
    @DisplayName("Create association successfully")
    void createAssociationOK() {
        RegisterDrugRequestBean requestBeanDrug = Instancio.of(RegisterDrugRequestBean.class)
                .generate(field(RegisterDrugRequestBean::getActiveIngredients), gen -> gen.collection().size(3))
                .create();
        String drugId = drugService.createDrug(requestBeanDrug).getDrugId();
        CreateActiveIngredientRequestBean requestBeanActiveIngredient = Instancio.of(CreateActiveIngredientRequestBean.class)
                .create();
        String activeIngredientId = drugService.createActiveIngredient(requestBeanActiveIngredient).getActiveIngredientId();
        CreateDrugActiveIngredientAssociationRequestBean requestBean = Instancio.of(CreateDrugActiveIngredientAssociationRequestBean.class)
                .set(field(CreateDrugActiveIngredientAssociationRequestBean::getDrugId), drugId)
                .set(field(CreateDrugActiveIngredientAssociationRequestBean::getActiveIngredientId), activeIngredientId)
                .create();
        CreateDrugActiveIngredientAssociationResponseBean serviceResponse = drugService.createAssociation(requestBean);
        log.info(serviceResponse.toString());

        assertResponseCodeEnum(ResponseCodesEnum.OK, serviceResponse);
    }

    @Test
    @DisplayName("Create association failing with drug not found")
    void createAssociationKODrugNotFound() {
        CreateActiveIngredientRequestBean requestBeanActiveIngredient = Instancio.of(CreateActiveIngredientRequestBean.class)
                .create();
        String activeIngredientId = drugService.createActiveIngredient(requestBeanActiveIngredient).getActiveIngredientId();
        CreateDrugActiveIngredientAssociationRequestBean requestBean = Instancio.of(CreateDrugActiveIngredientAssociationRequestBean.class)
                .set(field(CreateDrugActiveIngredientAssociationRequestBean::getActiveIngredientId), activeIngredientId)
                .create();
        CreateDrugActiveIngredientAssociationResponseBean serviceResponse = drugService.createAssociation(requestBean);
        log.info(serviceResponse.toString());

        assertResponseCodeEnum(ResponseCodesEnum.DRUG_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Create association failing with active ingredient not found")
    void createAssociationKOActiveIngredientNotFound() {
        RegisterDrugRequestBean requestBeanDrug = Instancio.of(RegisterDrugRequestBean.class)
                .generate(field(RegisterDrugRequestBean::getActiveIngredients), gen -> gen.collection().size(3))
                .create();
        String drugId = drugService.createDrug(requestBeanDrug).getDrugId();
        CreateDrugActiveIngredientAssociationRequestBean requestBean = Instancio.of(CreateDrugActiveIngredientAssociationRequestBean.class)
                .set(field(CreateDrugActiveIngredientAssociationRequestBean::getDrugId), drugId)
                .create();
        CreateDrugActiveIngredientAssociationResponseBean serviceResponse = drugService.createAssociation(requestBean);
        log.info(serviceResponse.toString());

        assertResponseCodeEnum(ResponseCodesEnum.ACTIVE_INGREDIENT_NOT_FOUND, serviceResponse);
    }

    @Test
    @DisplayName("Create association failing with ")
    void createAssociationKOAssociationAlreadyExists() {
        RegisterDrugRequestBean requestBeanDrug = Instancio.of(RegisterDrugRequestBean.class)
                .generate(field(RegisterDrugRequestBean::getActiveIngredients), gen -> gen.collection().size(3))
                .create();
        String drugId = drugService.createDrug(requestBeanDrug).getDrugId();
        CreateActiveIngredientRequestBean requestBeanActiveIngredient = Instancio.of(CreateActiveIngredientRequestBean.class)
                .create();
        String activeIngredientId = drugService.createActiveIngredient(requestBeanActiveIngredient).getActiveIngredientId();
        CreateDrugActiveIngredientAssociationRequestBean requestBean = Instancio.of(CreateDrugActiveIngredientAssociationRequestBean.class)
                .set(field(CreateDrugActiveIngredientAssociationRequestBean::getDrugId), drugId)
                .set(field(CreateDrugActiveIngredientAssociationRequestBean::getActiveIngredientId), activeIngredientId)
                .create();
        CreateDrugActiveIngredientAssociationResponseBean serviceResponse = drugService.createAssociation(requestBean);
        log.info(serviceResponse.toString());
        serviceResponse = drugService.createAssociation(requestBean);

        assertResponseCodeEnum(ResponseCodesEnum.ASSOCIATION_ALREADY_EXISTS, serviceResponse);
    }
}