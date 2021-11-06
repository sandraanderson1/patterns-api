package com.guardian.api.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "api.guardian")
public class GuardianProps implements DownstreamProps {
    private String apiKey ;
    private String baseUrl;

    @Override
    public String createDownstreamUrl() {
        return baseUrl + apiKey;
    }
}
