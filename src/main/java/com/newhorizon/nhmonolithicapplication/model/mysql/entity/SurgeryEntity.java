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

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SurgeryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String surgicalRoomId;
    @Column(nullable = false)
    private Instant surgeryStart;
    @Column(nullable = false)
    private Instant surgeryEnd;
    @Column(nullable = false)
    private String medicalChartId;
    @Column(nullable = false)
    private String surgeryTypeId;
    @Column(nullable = false)
    private Integer surgeryStatus;
}
