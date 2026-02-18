package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.DeviceDTO;

import java.util.Optional;

public interface DeviceDAO {
    Optional<DeviceDTO> findBySerialNumber(String serialNumber);

    Optional<DeviceDTO> registerDevice(DeviceDTO build);

    Optional<Integer> retrieveDeviceLastAssociationState(String serialNumber);

    Optional<DeviceDTO> assignDeviceToPatient(String serialNumber, String patientFiscalCode);

    Optional<DeviceDTO> unassignDeviceToPatient(String serialNumber, String patientFiscalCode);

    Boolean isDeviceAssignedToPatient(String serialNumber, String patientFiscalCode);

    Optional<DeviceDTO> updateDeviceEnablement(String deviceSerialNumber, Boolean isEnabled);
}
