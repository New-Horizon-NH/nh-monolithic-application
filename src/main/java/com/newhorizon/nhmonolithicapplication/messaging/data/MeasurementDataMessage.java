package com.newhorizon.nhmonolithicapplication.messaging.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class MeasurementDataMessage<T> {
    private String measurementType;
    private Long timestamp;
    private T content;
}
