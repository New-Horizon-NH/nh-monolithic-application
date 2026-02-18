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
public class MessageDataMessage<T> {
    private String messageType;
    private SourceDeviceDataMessage sourceDevice;
    private T content;
}
