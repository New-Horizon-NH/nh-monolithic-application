package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.SurgicalRoomDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.SurgicalRoomEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.SurgicalRoomRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class SurgicalRoomRepository implements SurgicalRoomDAO {
    private final SurgicalRoomRepo surgicalRoomRepo;

    @Override
    public Optional<SurgicalRoomDTO> findByRoomNumber(Integer roomNumber) {
        return surgicalRoomRepo.findByRoomNumber(roomNumber)
                .map(entity -> SurgicalRoomDTO.builder()
                        .roomId(entity.getId())
                        .roomNumber(entity.getRoomNumber())
                        .roomType(entity.getRoomType())
                        .build());
    }

    @Override
    public Optional<SurgicalRoomDTO> createSurgicalRoom(SurgicalRoomDTO build) {
        SurgicalRoomEntity entity = SurgicalRoomEntity.builder()
                .roomNumber(build.getRoomNumber())
                .roomType(build.getRoomType())
                .build();
        surgicalRoomRepo.saveAndFlush(entity);
        return Optional.of(build);
    }
}
