package com.newhorizon.nhmonolithicapplication.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "com.new-horizon.influx")
public class InfluxConfigurationProperties {
    private String databaseUrl;
    private Credentials credentials = new Credentials();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Credentials {
        private String username;
        private String password;

        public char[] getPasswordCharArray() {
            return getPassword().toCharArray();
        }
    }
}
