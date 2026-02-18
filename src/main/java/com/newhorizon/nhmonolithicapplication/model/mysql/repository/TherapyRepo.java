package com.newhorizon.nhmonolithicapplication.model.mysql.repository;

import com.newhorizon.nhmonolithicapplication.model.mysql.entity.TherapyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TherapyRepo extends JpaRepository<TherapyEntity, String> {
}
