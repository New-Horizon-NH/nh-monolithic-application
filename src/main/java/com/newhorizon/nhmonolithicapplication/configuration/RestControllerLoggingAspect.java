package com.newhorizon.nhmonolithicapplication.configuration;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class RestControllerLoggingAspect {
    @PostConstruct
    void logInit() {
        log.info("Init {}", this.getClass().getSimpleName());
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {
    }

    @Before("restController()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        final String logPatter = "[INCOMING REQUEST] - Method: {}, Request: {}";
        String method = joinPoint.getSignature().toShortString();
        String request = Arrays.toString(joinPoint.getArgs());
        log.info(logPatter, method, request);
    }

    @AfterReturning(pointcut = "restController()", returning = "response")
    public void logAfterMethod(JoinPoint joinPoint, Object response) {
        final String logPatter = "[OUTGOING RESPONSE] - Response: {}";
        log.info(logPatter, response);
    }
}