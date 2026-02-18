package com.newhorizon.nhmonolithicapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class DeviceDTO {
    private String deviceSerialNumber;
    private String deviceTlsCertificate;
    private Integer deviceType;
    private Boolean isEnabled;
}
