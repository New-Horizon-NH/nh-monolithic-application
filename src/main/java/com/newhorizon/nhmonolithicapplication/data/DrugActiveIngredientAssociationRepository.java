package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.DrugActiveIngredientAssociationDTO;
import com.newhorizon.nhmonolithicapplication.mapper.DrugActiveIngredientAssociationMapper;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.DrugActiveIngredientAssociationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class DrugActiveIngredientAssociationRepository implements DrugActiveIngredientAssociationDAO {
    private final DrugActiveIngredientAssociationRepo drugActiveIngredientAssociationRepo;
    private final DrugActiveIngredientAssociationMapper drugActiveIngredientAssociationMapper;

    @Override
    public Optional<DrugActiveIngredientAssociationDTO> retrieveAssociation(String drugId, String activeIngredientId) {
        return drugActiveIngredientAssociationRepo.findByDrugIdAndActiveIngredientId(drugId, activeIngredientId)
                .map(drugActiveIngredientAssociationMapper::parseEntity);
    }

    @Override
    public Optional<DrugActiveIngredientAssociationDTO> createAssociation(DrugActiveIngredientAssociationDTO build) {
        return Optional.of(drugActiveIngredientAssociationMapper.parseEntity(
                drugActiveIngredientAssociationRepo.saveAndFlush(
                        drugActiveIngredientAssociationMapper.parseDTO(build)
                )
        ));
    }
}
