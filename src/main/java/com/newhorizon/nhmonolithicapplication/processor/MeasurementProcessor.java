package com.newhorizon.nhmonolithicapplication.processor;


import com.newhorizon.nhmonolithicapplication.messaging.data.MeasurementDataMessage;
import com.newhorizon.nhmonolithicapplication.messaging.data.SourceDeviceDataMessage;
import com.newhorizon.nhmonolithicapplication.enums.MeasurementTypeEnum;

public interface MeasurementProcessor<T> {
    void process(MeasurementDataMessage<T> measurementDataMessage,
                 SourceDeviceDataMessage sourceDeviceDataMessage);

    MeasurementTypeEnum getMeasurementType();
}
