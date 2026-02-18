package com.newhorizon.nhmonolithicapplication.data;

import com.newhorizon.nhmonolithicapplication.dto.SurgeryDTO;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SurgeryDAO {
    List<SurgeryDTO> findOverlappingSurgeries(Integer surgicalRoomNumber,
                                              Instant startSurgery,
                                              Instant endSurgery);

    Optional<SurgeryDTO> scheduleSurgery(SurgeryDTO build);

    Optional<SurgeryDTO> retrieveScheduledSurgery(String surgeryId);

    Optional<SurgeryDTO> unscheduleSurgery(String surgeryId);

    Optional<SurgeryDTO> rescheduleSurgery(SurgeryDTO surgeryDTO);

}
