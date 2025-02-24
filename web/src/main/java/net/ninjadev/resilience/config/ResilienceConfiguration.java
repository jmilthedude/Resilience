package net.ninjadev.resilience.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "resilience")
public class ResilienceConfiguration {

    private String applicationName;
    private String defaultAccentColor;

}
