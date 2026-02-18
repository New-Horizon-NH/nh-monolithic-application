package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.DrugDTO;
import com.newhorizon.nhmonolithicapplication.mapper.DrugMapper;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.DrugRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class DrugRepository implements DrugDAO {
    private final DrugRepo drugRepo;
    private final DrugMapper drugMapper;

    @Override
    public Optional<DrugDTO> retrieveDrugByComplex(String drugName,
                                                   String pharmaceuticalCompany,
                                                   String pharmaceuticalForm,
                                                   String dosageFormDescription) {
        return drugRepo.findByNameAndPharmaceuticalCompanyAndPharmaceuticalFormAndDosageFormDescription(drugName,
                        pharmaceuticalCompany,
                        pharmaceuticalForm,
                        dosageFormDescription)
                .map(drugMapper::parseEntity);
    }

    @Override
    public Optional<DrugDTO> registerDrug(DrugDTO build) {
        return Optional.of(drugMapper.parseEntity(
                drugRepo.saveAndFlush(
                        drugMapper.parseDTO(build)
                )
        ));
    }

    @Override
    public Optional<DrugDTO> retrieveDrugById(String drugId) {
        return drugRepo.findById(drugId)
                .map(drugMapper::parseEntity);
    }
}
