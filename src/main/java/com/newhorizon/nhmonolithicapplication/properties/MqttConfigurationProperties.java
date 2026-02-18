package com.newhorizon.nhmonolithicapplication.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "com.new-horizon.mqtt")
public class MqttConfigurationProperties {
    private List<String> bootstrapServers;
    private String clientId;
    private Credentials credentials = new Credentials();
    private Map<String, String> topics;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Credentials {
        private String username;
        private String password;

        public char[] getPasswordCharArray() {
            return password.toCharArray();
        }
    }
}
