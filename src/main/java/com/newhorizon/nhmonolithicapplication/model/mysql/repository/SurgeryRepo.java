package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.SurgeryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface SurgeryRepo extends JpaRepository<SurgeryEntity, String> {
    @Query("SELECT surgery " +
            "FROM SurgeryEntity surgery " +
            "         JOIN SurgicalRoomEntity surgicalRoom " +
            "              ON surgicalRoom.id = surgery.surgicalRoomId " +
            "WHERE surgicalRoom.roomNumber = :surgeryRoomNumber " +
            "AND surgery.surgeryStatus != :notStatus " +
            "AND ((:surgeryStart <= surgery.surgeryStart AND :surgeryEnd >= surgery.surgeryEnd) " + //start before - end after
            "    OR (:surgeryStart <= surgery.surgeryStart AND " +
            "        (:surgeryEnd >= surgery.surgeryStart AND :surgeryEnd <= surgery.surgeryEnd)) " + //start before - end during
            "    OR ((:surgeryStart >= surgery.surgeryStart AND :surgeryStart <= surgery.surgeryEnd) AND " +
            "        (:surgeryEnd >= surgery.surgeryStart AND :surgeryEnd <= surgery.surgeryEnd)) " + // start during - end during
            "    OR ((:surgeryStart >= surgery.surgeryStart AND :surgeryStart <= surgery.surgeryEnd) AND " +
            "        :surgeryEnd >= surgery.surgeryEnd))" //start during - end after
    )
    List<SurgeryEntity> findOverlappingSurgeries(Integer surgeryRoomNumber,
                                                 Instant surgeryStart,
                                                 Instant surgeryEnd,
                                                 Integer notStatus);
}
