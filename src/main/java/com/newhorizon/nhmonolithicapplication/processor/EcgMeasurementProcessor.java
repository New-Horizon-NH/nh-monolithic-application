package com.newhorizon.nhmonolithicapplication.processor;

import com.newhorizon.nhmonolithicapplication.messaging.data.MeasurementDataMessage;
import com.newhorizon.nhmonolithicapplication.messaging.data.SourceDeviceDataMessage;
import com.newhorizon.nhmonolithicapplication.messaging.data.measurement.EcgMeasurement;
import com.newhorizon.nhmonolithicapplication.enums.MeasurementTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EcgMeasurementProcessor implements MeasurementProcessor<EcgMeasurement> {
    @Override
    public void process(MeasurementDataMessage<EcgMeasurement> measurementDataMessage,
                        SourceDeviceDataMessage sourceDeviceDataMessage) {
        log.info("Received {} measurement: {}",
                measurementDataMessage.getContent().getClass().getSimpleName(),
                measurementDataMessage.getContent().getValue());
    }

    @Override
    public MeasurementTypeEnum getMeasurementType() {
        return MeasurementTypeEnum.ECG;
    }
}
