package com.guardian.api.webClient;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@NonNull
@Getter
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "api.webclient.override")
public class OverrideWebClientProps extends WebClientProps {
}
