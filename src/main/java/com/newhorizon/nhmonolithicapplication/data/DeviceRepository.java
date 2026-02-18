package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.DeviceDTO;
import com.newhorizon.nhmonolithicapplication.enums.DeviceOperationTypeEnum;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DeviceEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.PatientDeviceAssociationEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.PatientEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.DeviceRepo;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.PatientDeviceAssociationRepo;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.PatientRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class DeviceRepository implements DeviceDAO {
    private final PatientRepo patientRepo;
    private final DeviceRepo deviceRepo;
    private final PatientDeviceAssociationRepo patientDeviceAssociationRepo;

    @Override
    public Optional<DeviceDTO> findBySerialNumber(String serialNumber) {
        return deviceRepo.findByDeviceSerialNumber(serialNumber)
                .map(entity -> DeviceDTO.builder()
                        .deviceSerialNumber(entity.getDeviceSerialNumber())
                        .deviceTlsCertificate(entity.getDeviceTlsCertificate())
                        .deviceType(entity.getDeviceType())
                        .isEnabled(entity.getIsEnabled())
                        .build());
    }

    @Override
    public Optional<DeviceDTO> registerDevice(DeviceDTO build) {
        DeviceEntity entity = DeviceEntity.builder()
                .deviceSerialNumber(build.getDeviceSerialNumber())
                .deviceTlsCertificate(build.getDeviceTlsCertificate())
                .deviceType(build.getDeviceType())
                .isEnabled(build.getIsEnabled())
                .build();
        deviceRepo.saveAndFlush(entity);
        return Optional.of(build);
    }

    @Override
    public Optional<Integer> retrieveDeviceLastAssociationState(String serialNumber) {
        return patientDeviceAssociationRepo.findLastAssociationByDeviceSerialNumber(serialNumber)
                .map(PatientDeviceAssociationEntity::getOperationType);
    }

    @Override
    public Optional<DeviceDTO> assignDeviceToPatient(String serialNumber, String patientFiscalCode) {
        DeviceEntity deviceEntity = deviceRepo.findByDeviceSerialNumber(serialNumber).orElseThrow();
        PatientEntity patientEntity = patientRepo.findByPatientFiscalCode(patientFiscalCode).orElseThrow();
        PatientDeviceAssociationEntity entity = PatientDeviceAssociationEntity.builder()
                .timestamp(Instant.now())
                .patientId(patientEntity.getId())
                .deviceId(deviceEntity.getId())
                .operationType(DeviceOperationTypeEnum.ASSIGNED.getDeviceOperationTypeCode())
                .build();
        patientDeviceAssociationRepo.saveAndFlush(entity);
        return Optional.of(DeviceDTO.builder()
                .deviceSerialNumber(deviceEntity.getDeviceSerialNumber())
                .deviceTlsCertificate(deviceEntity.getDeviceTlsCertificate())
                .deviceType(deviceEntity.getDeviceType())
                .isEnabled(deviceEntity.getIsEnabled())
                .build());
    }

    @Override
    public Optional<DeviceDTO> unassignDeviceToPatient(String serialNumber, String patientFiscalCode) {
        DeviceEntity deviceEntity = deviceRepo.findByDeviceSerialNumber(serialNumber).orElseThrow();
        PatientEntity patientEntity = patientRepo.findByPatientFiscalCode(patientFiscalCode).orElseThrow();
        PatientDeviceAssociationEntity entity = PatientDeviceAssociationEntity.builder()
                .timestamp(Instant.now())
                .patientId(patientEntity.getId())
                .deviceId(deviceEntity.getId())
                .operationType(DeviceOperationTypeEnum.RETURNED.getDeviceOperationTypeCode())
                .build();
        patientDeviceAssociationRepo.saveAndFlush(entity);
        return Optional.of(DeviceDTO.builder()
                .deviceSerialNumber(deviceEntity.getDeviceSerialNumber())
                .deviceTlsCertificate(deviceEntity.getDeviceTlsCertificate())
                .deviceType(deviceEntity.getDeviceType())
                .isEnabled(deviceEntity.getIsEnabled())
                .build());
    }

    @Override
    public Boolean isDeviceAssignedToPatient(String serialNumber, String patientFiscalCode) {
        PatientEntity patientEntity = patientRepo.findByPatientFiscalCode(patientFiscalCode).orElseThrow();
        PatientDeviceAssociationEntity patientDeviceAssociationEntity = patientDeviceAssociationRepo.findLastAssociationByDeviceSerialNumber(serialNumber).orElseThrow();
        return patientDeviceAssociationEntity.getPatientId().equals(patientEntity.getId());
    }

    @Override
    public Optional<DeviceDTO> updateDeviceEnablement(String deviceSerialNumber, Boolean isEnabled) {
        DeviceEntity entity = deviceRepo.findByDeviceSerialNumber(deviceSerialNumber).orElseThrow();
        entity.setIsEnabled(isEnabled);
        entity = deviceRepo.saveAndFlush(entity);
        return Optional.of(DeviceDTO.builder()
                .deviceSerialNumber(entity.getDeviceSerialNumber())
                .deviceTlsCertificate(entity.getDeviceTlsCertificate())
                .deviceType(entity.getDeviceType())
                .isEnabled(entity.getIsEnabled())
                .build());
    }
}
