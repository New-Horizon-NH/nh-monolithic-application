package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DepartmentRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DepartmentRoomRepo extends JpaRepository<DepartmentRoomEntity, String> {
    @Query("SELECT room " +
            "FROM DepartmentRoomEntity room " +
            "JOIN DepartmentEntity department ON room.departmentId=department.id " +
            "WHERE department.code=:departmentCode AND " +
            "room.roomNumber=:roomNumber")
    Optional<DepartmentRoomEntity> findByDepartmentCodeAndRoomNumber(String departmentCode,
                                                                     Integer roomNumber);

}
