package com.newhorizon.nhmonolithicapplication.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newhorizon.nhmonolithicapplication.enums.MeasurementTypeEnum;
import com.newhorizon.nhmonolithicapplication.enums.MessageTypeEnum;
import com.newhorizon.nhmonolithicapplication.factory.MeasurementProcessorFactory;
import com.newhorizon.nhmonolithicapplication.messaging.data.MeasurementDataMessage;
import com.newhorizon.nhmonolithicapplication.messaging.data.MessageDataMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommonMessageProcessor implements MessageHandler {
    private final ObjectMapper objectMapper;
    private final MeasurementProcessorFactory measurementProcessorFactory;

    @Override
    public void handleMessage(@NotNull Message<?> message) throws MessagingException {
        try {
            MessageDataMessage<?> messageDataMessage = objectMapper.readValue(String.valueOf(message.getPayload()), MessageDataMessage.class);
            switch (MessageTypeEnum.fromMessageType(messageDataMessage.getMessageType())) {
                case MEASUREMENT -> processMeasurement(messageDataMessage);
                case null, default -> processNull();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    private void processNull() {
        log.warn("Incompatible message type");
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private void processMeasurement(MessageDataMessage<?> messageDataMessage) {
        MeasurementDataMessage<?> measurementDataMessage = objectMapper.convertValue(messageDataMessage.getContent(), MeasurementDataMessage.class);
        Class<?> targetClass = MeasurementTypeEnum.fromName(measurementDataMessage.getMeasurementType()).getClazz();
        MeasurementDataMessage parsed = objectMapper.convertValue(measurementDataMessage, objectMapper.getTypeFactory()
                .constructParametricType(MeasurementDataMessage.class, targetClass));
        measurementProcessorFactory.getProcessor(measurementDataMessage.getMeasurementType())
                .process(parsed, messageDataMessage.getSourceDevice());
    }
}
