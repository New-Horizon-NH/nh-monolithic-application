package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.EmergencyRoomDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyRoomDocumentRepo extends JpaRepository<EmergencyRoomDocumentEntity, String> {
}
