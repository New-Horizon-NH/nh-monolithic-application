package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.BedDTO;

import java.util.Optional;

public interface DepartmentRoomBedDAO {
    Optional<BedDTO> createBed(BedDTO build);

    Optional<BedDTO> retrieveBedByBedNumberAndRoomNumberAndDepartmentCode(Integer bedNumber,
                                                                          Integer roomNumber,
                                                                          String departmentCode);

    Optional<Integer> retrieveBedLastAssociationState(Integer bedNumber,
                                                      Integer roomNumber,
                                                      String departmentCode);

    Optional<BedDTO> assignBedToPatient(Integer bedNumber,
                                        Integer roomNumber,
                                        String departmentCode,
                                        String userId);

    Boolean isBedAssignedToPatient(Integer bedNumber,
                                   Integer roomNumber,
                                   String departmentCode,
                                   String patientFiscalCode);

    Optional<BedDTO> unassignBedToPatient(Integer bedNumber,
                                          Integer roomNumber,
                                          String departmentCode,
                                          String patientFiscalCode);
}
