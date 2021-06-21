package com.guardian.api.webClient;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "api.webclient")
public class WebClientProps {
    @NonNull
    private Integer connectTimeoutMs;
    @NonNull
    private Integer connectionRequestTimeoutMs;
    @NonNull
    private Integer readTimeoutMs;
    @NonNull
    private Integer poolSize;
    @NonNull
    private Integer totalRequestTimeout;
}
