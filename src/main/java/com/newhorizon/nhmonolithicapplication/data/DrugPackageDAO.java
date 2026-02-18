package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.DrugPackageDTO;

import java.util.Optional;

public interface DrugPackageDAO {
    Optional<DrugPackageDTO> retrieveByPackageId(String packageId);

    Optional<DrugPackageDTO> createPackage(DrugPackageDTO build);

    Boolean isQuantityAvailable(String packageId, Long quantity);

    Optional<DrugPackageDTO> registerWithdraw(String packageId, Long quantity, String userId);

    Optional<DrugPackageDTO> addQuantity(String packageId, Long quantity);
}
