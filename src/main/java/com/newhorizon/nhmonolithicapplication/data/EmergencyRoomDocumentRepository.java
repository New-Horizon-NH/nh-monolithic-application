package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.EmergencyRoomDocumentDTO;
import com.newhorizon.nhmonolithicapplication.model.mysql.entity.EmergencyRoomDocumentEntity;
import com.newhorizon.nhmonolithicapplication.model.mysql.repository.EmergencyRoomDocumentRepo;
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
public class EmergencyRoomDocumentRepository implements EmergencyRoomDocumentDAO {
    private final EmergencyRoomDocumentRepo emergencyRoomDocumentRepo;

    @Override
    public Optional<EmergencyRoomDocumentDTO> saveDocument(EmergencyRoomDocumentDTO build) {
        EmergencyRoomDocumentEntity entity = EmergencyRoomDocumentEntity.builder()
                .medicalChartId(build.getMedicalChartId())
                .uploadFilePath(build.getUploadFilePath())
                .publishTimestamp(Instant.now())
                .build();
        emergencyRoomDocumentRepo.saveAndFlush(entity);
        return Optional.of(build);
    }
}
