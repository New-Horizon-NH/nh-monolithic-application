package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepo extends JpaRepository<DeviceEntity, String> {
    Optional<DeviceEntity> findByDeviceSerialNumber(String deviceSerialNumber);
}
