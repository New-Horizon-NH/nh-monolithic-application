package com.newhorizon.nhmonolithicapplication.model.mysql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TherapyRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String therapyId;
    @Column(nullable = false)
    private String packageId;
    @Column(nullable = false)
    private Integer administrationNumber;//when 0 it means when needed
    @Column(nullable = false)
    private Integer administrationType;
    @Column(nullable = false)
    private String medicalAssigneeId;
}
