package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.DrugDTO;

import java.util.Optional;

public interface DrugDAO {
    Optional<DrugDTO> retrieveDrugByComplex(String drugName,
                                            String pharmaceuticalCompany,
                                            String pharmaceuticalForm,
                                            String dosageFormDescription);

    Optional<DrugDTO> registerDrug(DrugDTO build);

    Optional<DrugDTO> retrieveDrugById(String drugId);
}
