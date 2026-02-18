package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DepartmentRoomBedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DepartmentRoomBedRepo extends JpaRepository<DepartmentRoomBedEntity, String> {
    @Query("SELECT bed " +
            "FROM DepartmentRoomBedEntity bed " +
            "JOIN DepartmentRoomEntity room " +
            "ON bed.roomId=room.id " +
            "JOIN DepartmentEntity department " +
            "ON department.id=room.departmentId " +
            "WHERE bed.bedNumber=:bedNumber AND " +
            "room.roomNumber=:roomNumber AND " +
            "department.code=:departmentCode")
    Optional<DepartmentRoomBedEntity> findByBedNumberAndRoomNumberAndDepartmentCode(Integer bedNumber,
                                                                                    Integer roomNumber,
                                                                                    String departmentCode);
}
