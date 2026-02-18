package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.TherapyDTO;

import java.util.Optional;

public interface TherapyDAO {
    Optional<TherapyDTO> createSUT(TherapyDTO build);

    Optional<TherapyDTO> retrieveById(String sutId);

}
