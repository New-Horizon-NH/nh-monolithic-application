package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.EmergencyRoomDocumentDTO;

import java.util.Optional;

public interface EmergencyRoomDocumentDAO {
    Optional<EmergencyRoomDocumentDTO> saveDocument(EmergencyRoomDocumentDTO build);

}
