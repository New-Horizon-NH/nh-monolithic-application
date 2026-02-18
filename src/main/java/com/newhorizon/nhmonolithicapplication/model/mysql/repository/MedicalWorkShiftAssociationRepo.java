package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.MedicalWorkShiftAssociationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MedicalWorkShiftAssociationRepo extends JpaRepository<MedicalWorkShiftAssociationEntity, String> {
    @Query("SELECT association " +
            "FROM MedicalWorkShiftAssociationEntity association " +
            "WHERE association.medicalId = :medicalId AND " +
            "association.shiftDate >= :startDate AND " +
            "association.shiftDate <= :endDate")
    List<MedicalWorkShiftAssociationEntity> findMonthShifts(String medicalId,
                                                            LocalDate startDate,
                                                            LocalDate endDate);
}
