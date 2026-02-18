package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.SurgeryTypeDTO;

import java.util.Optional;

public interface SurgeryTypeDAO {
    Optional<SurgeryTypeDTO> findBySurgeryCode(String surgeryCode);

    Optional<SurgeryTypeDTO> createSurgeryType(SurgeryTypeDTO build);
}
