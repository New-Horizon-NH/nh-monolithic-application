package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.WorkShiftDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.projections.MonthlyShiftProjectionImpl;

import java.util.List;
import java.util.Optional;

public interface WorkShiftDAO {
    Optional<WorkShiftDTO> retrieveByShiftCode(Integer shiftCode);


    List<MonthlyShiftProjectionImpl> retrieveMonthlyShifts(String medicalId, Integer month, Integer year);

    Optional<WorkShiftDTO> findById(String workShiftId);
}
