package com.newhorizon.nhmonolithicapplication.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MessageTypeEnum {
    MEASUREMENT("MEASUREMENT"),
    NETWORK("NETWORK");

    private final String messageType;

    public static MessageTypeEnum fromMessageType(String messageType) {
        Optional<MessageTypeEnum> messageTypeEnum = Arrays.stream(MessageTypeEnum.values())
                .filter(aEnum -> aEnum.getMessageType().equals(messageType))
                .findFirst();
        return messageTypeEnum.orElse(null);
    }
}
