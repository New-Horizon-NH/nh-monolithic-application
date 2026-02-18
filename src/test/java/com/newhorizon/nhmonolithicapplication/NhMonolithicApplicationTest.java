package com.newhorizon.nhmonolithicapplication;

import com.newhorizon.nhmonolithicapplication.beans.response.BaseResponse;
import com.newhorizon.nhmonolithicapplication.enums.ResponseCodesEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.InfluxDBContainer;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Testcontainers
@ActiveProfiles("local-test")
public class NhMonolithicApplicationTest {

    @ServiceConnection
    @SuppressWarnings("all")
    protected static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:9.0.1"))
            .withDatabaseName("nh-monolithic-application")
            .withUsername("root")
            .withPassword("P@ssw0rd");

    protected static final InfluxDBContainer<?> influxContainer = new InfluxDBContainer<>(DockerImageName.parse("influxdb:2"))
            .withUsername("root")
            .withPassword("P@ssw0rd")
            .withOrganization("new-horizon")
            .withBucket("nh-monolithic-application")
            .withAdminToken("P@ssw0rd");

    protected static final MinIOContainer minIOContainer = new MinIOContainer(DockerImageName.parse("minio/minio:RELEASE.2024-11-07T00-52-20Z"))
            .withEnv(Map.of(
                    "MINIO_ACCESS_KEY", "minioadmin",
                    "MINIO_ROOT_USER", "minioadmin",
                    "MINIO_SECRET_KEY", "minioadmin",
                    "MINIO_ROOT_PASSWORD", "minioadmin"
            ));

    protected static final RabbitMQContainer mqttContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3-management"))
            .withExposedPorts(5672, 15672, 1883);

    static {
        mysqlContainer.start();
        influxContainer.start();
        minIOContainer.start();
        mqttContainer.start();
        try {
            mqttContainer.execInContainer("rabbitmqctl add_user nh-monolithic-application P@ssw0rd");
            mqttContainer.execInContainer("rabbitmqctl set_user_tags nh-monolithic-application application");
            mqttContainer.execInContainer("rabbitmqctl set_permissions -p / nh-monolithic-application \\\".*\\\" \\\".*\\\" \\\".*\\\"");
            mqttContainer.execInContainer("rabbitmq-plugins enable rabbitmq_mqtt");
            mqttContainer.execInContainer("rabbitmq-plugins enable rabbitmq_web_mqtt");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("SPRING_DATASOURCE_URL", mysqlContainer::getJdbcUrl);
        registry.add("INFLUX_DATABASE_URL", influxContainer::getUrl);
        registry.add("MINIO_ENDPOINT_URL", minIOContainer::getS3URL);
        registry.add("COM_NEWHORIZON_MQTT_BOOTSTRAPSERVERS", () -> Collections.singletonList(MessageFormat.format("tcp://localhost:{0}", mqttContainer.getMappedPort(1883))));
    }

    @Test
    void contextLoads() {
        log.info("Using reusable context");
    }

    protected <T extends BaseResponse> void assertResponseCodeEnum(ResponseCodesEnum responseCodesEnum,
                                                                   T serviceResponse) {
        assertEquals(responseCodesEnum.getStatus(), serviceResponse.getStatus());
        assertEquals(responseCodesEnum.getErrorCode(), serviceResponse.getResponseCode());
        assertEquals(responseCodesEnum.getDescription(), serviceResponse.getResponseMessage());
    }
}
