package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.TherapyAdministrationRegistryDTO;
import com.newhorizon.nhmonolithicapplication.enums.AdministrationStatusEnum;

import java.util.List;
import java.util.Optional;

public interface TherapyAdministrationRegistryDAO {
    Optional<TherapyAdministrationRegistryDTO> register(TherapyAdministrationRegistryDTO build);

    List<TherapyAdministrationRegistryDTO> retrieveByTherapyRecord(String therapyRecordId);

    List<TherapyAdministrationRegistryDTO> retrieveByTherapyRecordWithState(String therapyRecordId,
                                                                            AdministrationStatusEnum statusEnum);
}
