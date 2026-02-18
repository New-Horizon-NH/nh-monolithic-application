package com.newhorizon.nhmonolithicapplication.factory;

import com.newhorizon.nhmonolithicapplication.processor.MeasurementProcessor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MeasurementProcessorFactory {
    private final List<MeasurementProcessor<?>> processors;

    public MeasurementProcessor<?> getProcessor(String measurementType) {
        Optional<MeasurementProcessor<?>> processor = processors.stream()
                .filter(p -> p.getMeasurementType().getEnumName().equals(measurementType))
                .findFirst();
        return processor.orElse(null);
    }

    @PostConstruct
    void logProcessors() {
        processors.forEach(p -> log.info(String.valueOf(p)));
    }
}
