package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.SurgicalRoomDTO;

import java.util.Optional;

public interface SurgicalRoomDAO {

    Optional<SurgicalRoomDTO> findByRoomNumber(Integer roomNumber);

    Optional<SurgicalRoomDTO> createSurgicalRoom(SurgicalRoomDTO build);
}
