package com.example.receiptvalidatorservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "import")
@Component
@Getter
@Setter
public class PathProperties {
    private String jsonSchema;
}
