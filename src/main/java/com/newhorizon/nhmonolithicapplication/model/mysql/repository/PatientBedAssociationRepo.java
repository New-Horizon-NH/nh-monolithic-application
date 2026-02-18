package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.PatientBedAssociationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PatientBedAssociationRepo extends JpaRepository<PatientBedAssociationEntity, String> {
    @Query("SELECT association " +
            "FROM PatientBedAssociationEntity association " +
            "JOIN DepartmentRoomBedEntity bed ON association.bedId=bed.id " +
            "JOIN DepartmentRoomEntity room ON bed.roomId=room.id " +
            "JOIN DepartmentEntity department ON department.id=room.departmentId " +
            "WHERE bed.bedNumber=:bedNumber AND " +
            "room.roomNumber=:roomNumber AND " +
            "department.code=:departmentCode " +
            "ORDER BY association.timestamp DESC " +
            "LIMIT 1")
    Optional<PatientBedAssociationEntity> findLastAssociationByBedNumberAndRoomNumberAndDepartmentCode(Integer bedNumber, Integer roomNumber, String departmentCode);

}
