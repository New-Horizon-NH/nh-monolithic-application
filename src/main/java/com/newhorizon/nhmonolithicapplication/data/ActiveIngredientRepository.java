package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.ActiveIngredientDTO;
import com.newhorizon.nhmonolithicapplication.mapper.ActiveIngredientMapper;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.DrugActiveIngredientRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class ActiveIngredientRepository implements ActiveIngredientDAO {
    private final DrugActiveIngredientRepo drugActiveIngredientRepo;
    private final ActiveIngredientMapper activeIngredientMapper;

    @Override
    public Optional<ActiveIngredientDTO> retrieveByName(String activeIngredient) {
        return drugActiveIngredientRepo.findByName(activeIngredient)
                .map(activeIngredientMapper::parseEntity);
    }

    @Override
    public Optional<ActiveIngredientDTO> createActiveIngredient(ActiveIngredientDTO build) {
        return Optional.of(activeIngredientMapper.parseEntity(
                drugActiveIngredientRepo.saveAndFlush(
                        activeIngredientMapper.parseDTO(build)
                )));
    }

    @Override
    public Optional<ActiveIngredientDTO> retrieveById(String drugId) {
        return drugActiveIngredientRepo.findById(drugId)
                .map(activeIngredientMapper::parseEntity);
    }
}
