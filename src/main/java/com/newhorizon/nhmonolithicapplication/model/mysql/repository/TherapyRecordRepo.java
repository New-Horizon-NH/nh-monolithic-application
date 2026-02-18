package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.TherapyRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TherapyRecordRepo extends JpaRepository<TherapyRecordEntity, String> {
    Boolean existsByTherapyIdAndPackageId(String therapyId, String packageId);
}
