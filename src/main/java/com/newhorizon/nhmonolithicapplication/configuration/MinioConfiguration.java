package com.newhorizon.nhmonolithicapplication.configuration;

import com.newhorizon.nhmonolithicapplication.properties.MinioConfigurationProperties;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MinioConfiguration {
    @Bean
    @Primary
    public MinioClient minioClient(MinioConfigurationProperties properties) {
        return MinioClient.builder()
                .endpoint(properties.getEndpointUrl())
                .credentials(properties.getCredentials().getAccessKey(), properties.getCredentials().getSecretKey())
                .build();
    }
}
