package com.newhorizon.nhmonolithicapplication.enums;

import com.newhorizon.nhmonolithicapplication.messaging.data.measurement.BpmMeasurement;
import com.newhorizon.nhmonolithicapplication.messaging.data.measurement.EcgMeasurement;
import com.newhorizon.nhmonolithicapplication.messaging.data.measurement.NibpMeasurement;
import com.newhorizon.nhmonolithicapplication.messaging.data.measurement.RespMeasurement;
import com.newhorizon.nhmonolithicapplication.messaging.data.measurement.SpO2Measurement;
import com.newhorizon.nhmonolithicapplication.messaging.data.measurement.TempMeasurement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MeasurementTypeEnum {
    ECG("ECG", EcgMeasurement.class),
    NIBP("NIBP", NibpMeasurement.class),
    RESP("RESP", RespMeasurement.class),
    SPO2("SpO2", SpO2Measurement.class),
    TEMP("TEMP", TempMeasurement.class),
    BPM("BPM", BpmMeasurement.class);

    private final String enumName;
    private final Class<?> clazz;

    public static MeasurementTypeEnum fromName(String name) {
        Optional<MeasurementTypeEnum> measurementTypeEnum = Arrays.stream(MeasurementTypeEnum.values())
                .filter(aEnum -> aEnum.getEnumName().equals(name))
                .findFirst();
        return measurementTypeEnum.orElse(null);
    }
}
