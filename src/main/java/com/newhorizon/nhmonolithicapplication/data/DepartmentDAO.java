package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.DepartmentDTO;
import com.newhorizon.nhmonolithicapplication.dto.RoomDTO;

import java.util.Optional;

public interface DepartmentDAO {
    Optional<DepartmentDTO> retrieveByCode(String code);

    Optional<DepartmentDTO> createDepartment(DepartmentDTO build);

}
