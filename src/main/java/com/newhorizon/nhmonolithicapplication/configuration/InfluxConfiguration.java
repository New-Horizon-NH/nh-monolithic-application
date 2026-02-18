package com.newhorizon.nhmonolithicapplication.configuration;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.newhorizon.nhmonolithicapplication.properties.InfluxConfigurationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class InfluxConfiguration {
    @Bean
    @Primary
    public InfluxDBClient influxDb(InfluxConfigurationProperties properties) {
        return InfluxDBClientFactory.create(properties.getDatabaseUrl(),
                properties.getCredentials().getUsername(),
                properties.getCredentials().getPasswordCharArray());
    }

    @Bean
    @Primary
    public WriteApiBlocking writeApi(InfluxDBClient client) {
        return client.getWriteApiBlocking();
    }

}
