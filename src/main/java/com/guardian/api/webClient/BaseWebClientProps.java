package com.guardian.api.webClient;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@NonNull
@Component
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "api.webclient.base")
public class BaseWebClientProps extends WebClientProps {
}
