package com.newhorizon.nhmonolithicapplication.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SurgicalRoomTypeEnum {
    GENERIC(1000, "Generic"),
    ORTHOPEDIC(1001, "Orthopedic"),
    CARDIAC_SURGERY(1002, "Cardiac Surgery"),
    NEUROSURGERY(1003, "Neurosurgery"),
    PLASTIC_AND_RECONSTRUCTIVE_SURGERY(1004, "Plastic and Reconstructive Surgery"),
    OPHTHALMIC(1005, "Ophthalmic"),
    LAPAROSCOPIC(1006, "Laparoscopic"),
    ROBOTICS(1007, "Robotics"),
    PEDIATRIC(1008, "Pediatric"),
    GYNECOLOGY_AND_OBSTETRICS(1009, "Gynecology and Obstetrics"),
    THORACIC(1010, "Thoracic"),
    MAXILLOFACIAL(1011, "Maxillofacial"),
    UROLOGICAL(1012, "Urological"),
    VASCULAR(1013, "Vascular"),
    DAY_SURGERY(1014, "Day Surgery"), //ambulatory
    HYBRID(1015, "Hybrid");
    private final Integer surgicalRoomTypeCode;
    private final String surgicalRoomTypeName;

    public static SurgicalRoomTypeEnum getTypeByCode(Integer code) {
        Optional<SurgicalRoomTypeEnum> operation = Arrays.stream(SurgicalRoomTypeEnum.values())
                .filter(surgicalRoomTypeEnum -> code.equals(surgicalRoomTypeEnum.getSurgicalRoomTypeCode()))
                .findFirst();
        return operation.orElse(null);
    }
}
