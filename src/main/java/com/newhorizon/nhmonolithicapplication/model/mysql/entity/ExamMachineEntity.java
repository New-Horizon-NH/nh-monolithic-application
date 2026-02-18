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
public class ExamMachineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String examTypeId;
    @Column(nullable = false)
    private String machineSerialNumber;
    @Column(nullable = false)
    private String machineName;
    @Column(nullable = false,
            columnDefinition = "BIT(1) NOT NULL DAFAULT 0")
    private Boolean isEnabled;
}
