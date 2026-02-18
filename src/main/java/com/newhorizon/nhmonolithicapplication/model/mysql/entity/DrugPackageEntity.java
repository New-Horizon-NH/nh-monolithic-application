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
public class DrugPackageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String drugId;
    @Column(nullable = false,
            unique = true)
    private String packageId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String aicCode;
    @Column(nullable = false)
    private String fornitureClass;
    @Column(nullable = false)
    private String fornitureClassDescription;
    @Column(nullable = false)
    private String refundabilityClass;
    @Column(nullable = false)
    private Long quantity;
}
