package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.PatientDeviceAssociationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PatientDeviceAssociationRepo extends JpaRepository<PatientDeviceAssociationEntity, String> {
    @Query("SELECT association " +
            "FROM PatientDeviceAssociationEntity association " +
            "JOIN DeviceEntity device ON association.deviceId=device.id " +
            "WHERE device.deviceSerialNumber=:serialNumber " +
            "ORDER BY association.timestamp DESC " +
            "LIMIT 1")
    Optional<PatientDeviceAssociationEntity> findLastAssociationByDeviceSerialNumber(String serialNumber);
}
