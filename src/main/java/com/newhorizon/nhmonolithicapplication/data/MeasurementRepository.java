package com.newhorizon.nhmonolithicapplication.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class MeasurementRepository implements MeasurementDAO{
}
