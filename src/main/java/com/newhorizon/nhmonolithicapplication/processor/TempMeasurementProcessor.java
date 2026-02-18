package com.newhorizon.nhmonolithicapplication.processor;

import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.newhorizon.nhmonolithicapplication.messaging.data.MeasurementDataMessage;
import com.newhorizon.nhmonolithicapplication.messaging.data.SourceDeviceDataMessage;
import com.newhorizon.nhmonolithicapplication.messaging.data.measurement.TempMeasurement;
import com.newhorizon.nhmonolithicapplication.enums.MeasurementTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TempMeasurementProcessor implements MeasurementProcessor<TempMeasurement> {
    private final WriteApiBlocking writeApi;

    @Override
    public void process(MeasurementDataMessage<TempMeasurement> measurementDataMessage,
                        SourceDeviceDataMessage sourceDeviceDataMessage) {
        log.info("Received {} measurement: {}",
                measurementDataMessage.getContent().getClass().getSimpleName(),
                measurementDataMessage.getContent().getValue());
        writeApi.writePoint(Point.measurement(getMeasurementType().getEnumName())
                .addTag("deviceId", sourceDeviceDataMessage.getDeviceId())
                .addField("temp", measurementDataMessage.getContent().getValue())
                .time(measurementDataMessage.getTimestamp(), WritePrecision.NS));
    }

    @Override
    public MeasurementTypeEnum getMeasurementType() {
        return MeasurementTypeEnum.TEMP;
    }
}
