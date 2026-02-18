package com.newhorizon.nhmonolithicapplication.service;

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
import com.newhorizon.nhmonolithicapplication.beans.response.UpdateDrugFornitureResponseBean;
import com.newhorizon.nhmonolithicapplication.beans.response.WithdrawDrugResponseBean;
import com.newhorizon.nhmonolithicapplication.data.ActiveIngredientDAO;
import com.newhorizon.nhmonolithicapplication.data.DrugActiveIngredientAssociationDAO;
import com.newhorizon.nhmonolithicapplication.data.DrugDAO;
import com.newhorizon.nhmonolithicapplication.data.DrugPackageDAO;
import com.newhorizon.nhmonolithicapplication.dto.ActiveIngredientDTO;
import com.newhorizon.nhmonolithicapplication.dto.DrugActiveIngredientAssociationDTO;
import com.newhorizon.nhmonolithicapplication.dto.DrugDTO;
import com.newhorizon.nhmonolithicapplication.dto.DrugPackageDTO;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DrugServiceImpl implements DrugService {
    private final ActiveIngredientDAO activeIngredientDAO;
    private final DrugDAO drugDAO;
    private final DrugPackageDAO drugPackageDAO;
    private final DrugActiveIngredientAssociationDAO drugActiveIngredientAssociationDAO;

    @Override
    @SuppressWarnings("unchecked")
    public CreateActiveIngredientResponseBean createActiveIngredient(CreateActiveIngredientRequestBean requestBean) {
        if (activeIngredientDAO.retrieveByName(requestBean.getActiveIngredient()).isPresent()) {
            return CreateActiveIngredientResponseBean.builder()
                    .status(ResponseCodesEnum.ACTIVE_INGREDIENT_ALREADY_EXISTS.getStatus())
                    .responseCode(ResponseCodesEnum.ACTIVE_INGREDIENT_ALREADY_EXISTS.getErrorCode())
                    .responseMessage(ResponseCodesEnum.ACTIVE_INGREDIENT_ALREADY_EXISTS.getDescription())
                    .build();
        }
        Optional<ActiveIngredientDTO> saved = activeIngredientDAO.createActiveIngredient(ActiveIngredientDTO.builder()
                .name(requestBean.getActiveIngredient())
                .build());
        if (saved.isEmpty()) {
            return CreateActiveIngredientResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return CreateActiveIngredientResponseBean.builder()
                .activeIngredientId(saved.get().getId())
                .activeIngredient(saved.get().getName())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RegisterDrugResponseBean createDrug(RegisterDrugRequestBean requestBean) {
        if (drugDAO.retrieveDrugByComplex(requestBean.getDrugName(),
                requestBean.getPharmaceuticalCompany(),
                requestBean.getPharmaceuticalForm(),
                requestBean.getDosageFormDescription()).isPresent()) {
            return RegisterDrugResponseBean.builder()
                    .status(ResponseCodesEnum.DRUG_ALREADY_EXISTS.getStatus())
                    .responseCode(ResponseCodesEnum.DRUG_ALREADY_EXISTS.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DRUG_ALREADY_EXISTS.getDescription())
                    .build();
        }
        Optional<DrugDTO> saved = drugDAO.registerDrug(DrugDTO.builder()
                .drugName(requestBean.getDrugName())
                .drugCode(requestBean.getDrugCode())
                .pharmaceuticalCompany(requestBean.getPharmaceuticalCompany())
                .pharmaceuticalForm(requestBean.getPharmaceuticalForm())
                .dosageFormDescription(requestBean.getDosageFormDescription())
                .build());
        if (saved.isEmpty()) {
            return RegisterDrugResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        List<String> associations = requestBean.getActiveIngredients()
                .stream()
                .map(activeIngredient -> idempotentCreateActiveIngredient(CreateActiveIngredientRequestBean.builder()
                        .activeIngredient(activeIngredient)
                        .build())
                        .getActiveIngredientId())
                .map(activeIngredientId -> idempotentCreateAssociation(CreateDrugActiveIngredientAssociationRequestBean.builder()
                        .drugId(saved.get().getDrugId())
                        .activeIngredientId(activeIngredientId)
                        .build())
                        .getAssociationId())
                .toList();
        return RegisterDrugResponseBean.builder()
                .drugId(saved.get().getDrugId())
                .name(saved.get().getDrugName())
                .activeIngredientsAssociation(associations)
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CreateDrugPackageResponseBean createDrugPackage(CreateDrugPackageRequestBean requestBean) {
        if (drugDAO.retrieveDrugById(requestBean.getDrugId()).isEmpty()) {
            return CreateDrugPackageResponseBean.builder()
                    .status(ResponseCodesEnum.DRUG_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.DRUG_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DRUG_NOT_FOUND.getDescription())
                    .build();
        }
        if (drugPackageDAO.retrieveByPackageId(requestBean.getPackageId()).isPresent()) {
            return CreateDrugPackageResponseBean.builder()
                    .status(ResponseCodesEnum.DRUG_PACKAGE_ALREADY_EXISTS.getStatus())
                    .responseCode(ResponseCodesEnum.DRUG_PACKAGE_ALREADY_EXISTS.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DRUG_PACKAGE_ALREADY_EXISTS.getDescription())
                    .build();
        }
        Optional<DrugPackageDTO> saved = drugPackageDAO.createPackage(DrugPackageDTO.builder()
                .drugId(requestBean.getDrugId())
                .packageId(requestBean.getPackageId())
                .name(requestBean.getName())
                .aicCode(requestBean.getAicCode())
                .fornitureClass(requestBean.getFornitureClass())
                .fornitureClassDescription(requestBean.getFornitureClassDescription())
                .refundabilityClass(requestBean.getRefundabilityClass())
                .build());
        if (saved.isEmpty()) {
            return CreateDrugPackageResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return CreateDrugPackageResponseBean.builder()
                .packageId(saved.get().getPackageId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public WithdrawDrugResponseBean withdrawDrugPackage(WithdrawDrugRequestBean requestBean) {
        if (drugPackageDAO.retrieveByPackageId(requestBean.getPackageId()).isEmpty()) {
            return WithdrawDrugResponseBean.builder()
                    .status(ResponseCodesEnum.DRUG_PACKAGE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.DRUG_PACKAGE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DRUG_PACKAGE_NOT_FOUND.getDescription())
                    .build();
        }
        if (Boolean.FALSE.equals(drugPackageDAO.isQuantityAvailable(requestBean.getPackageId(), requestBean.getQuantity()))) {
            return WithdrawDrugResponseBean.builder()
                    .status(ResponseCodesEnum.DRUG_QUANTITY_NOT_AVAILABLE.getStatus())
                    .responseCode(ResponseCodesEnum.DRUG_QUANTITY_NOT_AVAILABLE.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DRUG_QUANTITY_NOT_AVAILABLE.getDescription())
                    .build();
        }
        Optional<DrugPackageDTO> saved = drugPackageDAO.registerWithdraw(requestBean.getPackageId(),
                requestBean.getQuantity(),
                requestBean.getUserId());
        if (saved.isEmpty()) {
            return WithdrawDrugResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return WithdrawDrugResponseBean.builder()
                .packageId(saved.get().getPackageId())
                .quantity(requestBean.getQuantity())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CreateActiveIngredientResponseBean idempotentCreateActiveIngredient(CreateActiveIngredientRequestBean requestBean) {
        Optional<ActiveIngredientDTO> optional = activeIngredientDAO.retrieveByName(requestBean.getActiveIngredient());
        if (optional.isEmpty()) {
            optional = activeIngredientDAO.createActiveIngredient(ActiveIngredientDTO.builder()
                    .name(requestBean.getActiveIngredient())
                    .build());
        }
        return CreateActiveIngredientResponseBean.builder()
                .activeIngredientId(optional.orElseThrow().getId())
                .activeIngredient(optional.orElseThrow().getName())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CreateDrugActiveIngredientAssociationResponseBean createAssociation(CreateDrugActiveIngredientAssociationRequestBean requestBean) {
        if (drugDAO.retrieveDrugById(requestBean.getDrugId()).isEmpty()) {
            return CreateDrugActiveIngredientAssociationResponseBean.builder()
                    .status(ResponseCodesEnum.DRUG_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.DRUG_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DRUG_NOT_FOUND.getDescription())
                    .build();
        }
        if (activeIngredientDAO.retrieveById(requestBean.getActiveIngredientId()).isEmpty()) {
            return CreateDrugActiveIngredientAssociationResponseBean.builder()
                    .status(ResponseCodesEnum.ACTIVE_INGREDIENT_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.ACTIVE_INGREDIENT_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.ACTIVE_INGREDIENT_NOT_FOUND.getDescription())
                    .build();
        }
        if (drugActiveIngredientAssociationDAO.retrieveAssociation(requestBean.getDrugId(),
                requestBean.getActiveIngredientId()).isPresent()) {
            return CreateDrugActiveIngredientAssociationResponseBean.builder()
                    .status(ResponseCodesEnum.ASSOCIATION_ALREADY_EXISTS.getStatus())
                    .responseCode(ResponseCodesEnum.ASSOCIATION_ALREADY_EXISTS.getErrorCode())
                    .responseMessage(ResponseCodesEnum.ASSOCIATION_ALREADY_EXISTS.getDescription())
                    .build();
        }
        Optional<DrugActiveIngredientAssociationDTO> saved = drugActiveIngredientAssociationDAO.createAssociation(DrugActiveIngredientAssociationDTO.builder()
                .drugId(requestBean.getDrugId())
                .activeIngredientId(requestBean.getActiveIngredientId())
                .build());
        if (saved.isEmpty()) {
            return CreateDrugActiveIngredientAssociationResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return CreateDrugActiveIngredientAssociationResponseBean.builder()
                .associationId(saved.get().getId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CreateDrugActiveIngredientAssociationResponseBean idempotentCreateAssociation(CreateDrugActiveIngredientAssociationRequestBean requestBean) {
        Optional<DrugActiveIngredientAssociationDTO> optional = drugActiveIngredientAssociationDAO.retrieveAssociation(requestBean.getDrugId(),
                requestBean.getActiveIngredientId());
        if (optional.isEmpty()) {
            optional = drugActiveIngredientAssociationDAO.createAssociation(DrugActiveIngredientAssociationDTO.builder()
                    .drugId(requestBean.getDrugId())
                    .activeIngredientId(requestBean.getActiveIngredientId())
                    .build());
        }
        return CreateDrugActiveIngredientAssociationResponseBean.builder()
                .associationId(optional.orElseThrow().getId())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public UpdateDrugFornitureResponseBean updateDrugForniture(UpdateDrugFornitureRequestBean requestBean) {
        if (drugPackageDAO.retrieveByPackageId(requestBean.getPackageId()).isEmpty()) {
            return UpdateDrugFornitureResponseBean.builder()
                    .status(ResponseCodesEnum.DRUG_PACKAGE_NOT_FOUND.getStatus())
                    .responseCode(ResponseCodesEnum.DRUG_PACKAGE_NOT_FOUND.getErrorCode())
                    .responseMessage(ResponseCodesEnum.DRUG_PACKAGE_NOT_FOUND.getDescription())
                    .build();
        }
        Optional<DrugPackageDTO> drugPackageDTO = drugPackageDAO.addQuantity(requestBean.getPackageId(), requestBean.getQuantity());
        if (drugPackageDTO.isEmpty()) {
            return UpdateDrugFornitureResponseBean.builder()
                    .status(ResponseCodesEnum.GENERIC_ERROR.getStatus())
                    .responseCode(ResponseCodesEnum.GENERIC_ERROR.getErrorCode())
                    .responseMessage(ResponseCodesEnum.GENERIC_ERROR.getDescription())
                    .build();
        }
        return UpdateDrugFornitureResponseBean.builder()
                .build();
    }
}
