package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.RoomDTO;

import java.util.Optional;

public interface DepartmentRoomDAO {
    Optional<RoomDTO> retrieveRoomByDepartmentAndNumber(String departmentCode,
                                                        Integer roomNumber);

    Optional<RoomDTO> createRoom(RoomDTO build);
}
