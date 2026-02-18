package com.newhorizon.nhmonolithicapplication.configuration;

import com.influxdb.client.InfluxDBClient;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ResourceShutdownConfiguration {
    private final List<InfluxDBClient> influxDBClients;
    private final List<MinioClient> minioClients;

    @PostConstruct
    void addShutdownHook() {
        registerShutdownHook(influxDBClients,
                "InfluxConnectionPool");
        registerShutdownHook(minioClients,
                "MinioConnectionPool");
    }

    private void registerShutdownHook(List<? extends AutoCloseable> closeableList,
                                      String connectionPoolName) {
        Runtime.getRuntime()
                .addShutdownHook(new Thread(() -> invokeShutdownHook(closeableList, connectionPoolName)));
    }

    private void invokeShutdownHook(List<? extends AutoCloseable> closeableList,
                                    String connectionPoolName) {
        log.info("{} - Shutdown initiated...", connectionPoolName);
        closeableList.forEach(c -> {
            try {
                c.close();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
        log.info("{} - Shutdown completed.", connectionPoolName);
    }
}
