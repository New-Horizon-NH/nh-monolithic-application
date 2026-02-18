package com.newhorizon.nhmonolithicapplication.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "com.new-horizon.documents.upload")
public class DocumentUploadConfigurationProperties {
    private String examResult;
    private String emergencyRoomDocument;
}
