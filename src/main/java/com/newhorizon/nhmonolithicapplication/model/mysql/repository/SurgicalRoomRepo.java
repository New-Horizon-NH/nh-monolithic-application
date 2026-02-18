package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.SurgicalRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurgicalRoomRepo extends JpaRepository<SurgicalRoomEntity, String> {
    Optional<SurgicalRoomEntity> findByRoomNumber(Integer roomNumber);
}
