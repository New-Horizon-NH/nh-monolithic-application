package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.WorkShiftEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.projections.MonthlyShiftProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkShiftRepo extends JpaRepository<WorkShiftEntity, String> {
    Optional<WorkShiftEntity> findByShiftCode(Integer shiftCode);

    @Query("SELECT association.shiftDate as shiftDate," +
            "shift.shiftCode as shiftCode " +
            "FROM WorkShiftEntity shift " +
            "JOIN MedicalWorkShiftAssociationEntity association ON shift.id=association.workShiftId " +
            "WHERE association.medicalId=:medicalId AND " +
            "association.shiftDate >= :monthStart AND " +
            "association.shiftDate <= :monthEnd")
    List<MonthlyShiftProjection> findMonthlyShifts(String medicalId,
                                                   LocalDate monthStart,
                                                   LocalDate monthEnd);
}
