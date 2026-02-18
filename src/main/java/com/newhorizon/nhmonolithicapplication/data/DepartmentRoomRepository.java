package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.RoomDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.DepartmentRoomEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.DepartmentRepo;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.DepartmentRoomRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class DepartmentRoomRepository implements DepartmentRoomDAO {
    private final DepartmentRoomRepo departmentRoomRepo;
    private final DepartmentRepo departmentRepo;

    @Override
    public Optional<RoomDTO> retrieveRoomByDepartmentAndNumber(String departmentCode, Integer roomNumber) {
        return departmentRoomRepo.findByDepartmentCodeAndRoomNumber(departmentCode, roomNumber)
                .map(entity -> RoomDTO.builder()
                        .departmentCode(departmentCode)
                        .roomNumber(entity.getRoomNumber())
                        .bedCount(entity.getBedCount())
                        .build());
    }

    @Override
    public Optional<RoomDTO> createRoom(RoomDTO build) {
        DepartmentRoomEntity entity = DepartmentRoomEntity.builder()
                .departmentId(departmentRepo.findByCode(build.getDepartmentCode())
                        .orElseThrow()
                        .getId())
                .roomNumber(build.getRoomNumber())
                .bedCount(build.getBedCount())
                .build();
        departmentRoomRepo.saveAndFlush(entity);
        return Optional.of(build);
    }
}
