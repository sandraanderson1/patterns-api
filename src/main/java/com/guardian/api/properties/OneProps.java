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
@ConfigurationProperties(prefix = "api.one")
public class OneProps implements DownstreamProps {
    private String baseUrl;

    @Override
    public String createDownstreamUrl() {
        return baseUrl;
    }
}
