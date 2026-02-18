package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.BedDTO;
import com.newhorizon.nhmonolithicapplication.enums.BedOperationTypeEnum;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DepartmentRoomBedEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.PatientBedAssociationEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.PatientEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.DepartmentRoomBedRepo;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.DepartmentRoomRepo;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.PatientBedAssociationRepo;
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
public class DepartmentRoomBedRepository implements DepartmentRoomBedDAO {
    private final DepartmentRoomRepo departmentRoomRepo;
    private final DepartmentRoomBedRepo departmentRoomBedRepo;
    private final PatientBedAssociationRepo patientBedAssociationRepo;
    private final PatientRepo patientRepo;

    @Override
    public Optional<BedDTO> createBed(BedDTO build) {
        DepartmentRoomBedEntity entity = DepartmentRoomBedEntity.builder()
                .roomId(departmentRoomRepo.findByDepartmentCodeAndRoomNumber(build.getDepartmentCode(), build.getRoomNumber()).orElseThrow().getId())
                .bedNumber(build.getBedNumber())
                .isMotorized(build.getIsMotorized())
                .bedSerialNumber(build.getBedSerialNumber())
                .build();
        departmentRoomBedRepo.saveAndFlush(entity);
        return Optional.of(build);
    }

    public Optional<BedDTO> retrieveBedByBedNumberAndRoomNumberAndDepartmentCode(Integer bedNumber,
                                                                                 Integer roomNumber,
                                                                                 String departmentCode) {
        return departmentRoomBedRepo.findByBedNumberAndRoomNumberAndDepartmentCode(bedNumber, roomNumber, departmentCode)
                .map(entity -> BedDTO.builder()
                        .departmentCode(departmentCode)
                        .roomNumber(roomNumber)
                        .bedNumber(bedNumber)
                        .isMotorized(entity.getIsMotorized())
                        .bedSerialNumber(entity.getBedSerialNumber())
                        .build());
    }

    @Override
    public Optional<Integer> retrieveBedLastAssociationState(Integer bedNumber,
                                                             Integer roomNumber,
                                                             String departmentCode) {
        return patientBedAssociationRepo.findLastAssociationByBedNumberAndRoomNumberAndDepartmentCode(bedNumber,
                        roomNumber,
                        departmentCode)
                .map(PatientBedAssociationEntity::getOperationType);
    }

    @Override
    public Optional<BedDTO> assignBedToPatient(Integer bedNumber,
                                               Integer roomNumber,
                                               String departmentCode,
                                               String patientFiscalCode) {
        DepartmentRoomBedEntity departmentRoomBedEntity = departmentRoomBedRepo.findByBedNumberAndRoomNumberAndDepartmentCode(bedNumber, roomNumber, departmentCode).orElseThrow();
        PatientEntity patientEntity = patientRepo.findByPatientFiscalCode(patientFiscalCode).orElseThrow();
        PatientBedAssociationEntity entity = PatientBedAssociationEntity.builder()
                .timestamp(Instant.now())
                .patientId(patientEntity.getId())
                .bedId(departmentRoomBedEntity.getId())
                .operationType(BedOperationTypeEnum.ASSIGNED.getDeviceOperationTypeCode())
                .build();
        patientBedAssociationRepo.saveAndFlush(entity);
        return Optional.of(BedDTO.builder()
                .departmentCode(departmentCode)
                .bedNumber(bedNumber)
                .isMotorized(departmentRoomBedEntity.getIsMotorized())
                .bedSerialNumber(departmentRoomBedEntity.getBedSerialNumber())
                .roomNumber(roomNumber)
                .build());
    }

    @Override
    public Boolean isBedAssignedToPatient(Integer bedNumber, Integer roomNumber, String departmentCode, String patientFiscalCode) {
        PatientEntity patientEntity = patientRepo.findByPatientFiscalCode(patientFiscalCode).orElseThrow();
        PatientBedAssociationEntity patientBedAssociationEntity = patientBedAssociationRepo.findLastAssociationByBedNumberAndRoomNumberAndDepartmentCode(bedNumber, roomNumber, departmentCode).orElseThrow();
        return patientBedAssociationEntity.getPatientId().equals(patientEntity.getId());
    }

    @Override
    public Optional<BedDTO> unassignBedToPatient(Integer bedNumber, Integer roomNumber, String departmentCode, String patientFiscalCode) {
        PatientEntity patientEntity = patientRepo.findByPatientFiscalCode(patientFiscalCode).orElseThrow();
        DepartmentRoomBedEntity departmentRoomBedEntity = departmentRoomBedRepo.findByBedNumberAndRoomNumberAndDepartmentCode(bedNumber, roomNumber, departmentCode).orElseThrow();
        PatientBedAssociationEntity entity = PatientBedAssociationEntity.builder()
                .timestamp(Instant.now())
                .patientId(patientEntity.getId())
                .bedId(departmentRoomBedEntity.getId())
                .operationType(BedOperationTypeEnum.RETURNED.getDeviceOperationTypeCode())
                .build();
        patientBedAssociationRepo.saveAndFlush(entity);
        return Optional.of(BedDTO.builder()
                .departmentCode(departmentCode)
                .roomNumber(roomNumber)
                .bedNumber(bedNumber)
                .isMotorized(departmentRoomBedEntity.getIsMotorized())
                .bedSerialNumber(departmentRoomBedEntity.getBedSerialNumber())
                .build());
    }
}
