package com.guardian.api;

import com.guardian.api.webClient.BaseWebClientProps;
import com.guardian.api.webClient.OverrideWebClientProps;
import com.guardian.api.webClient.WebClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DownstreamHttpConfiguration {

    @Autowired
    @Bean("base")
    public WebClient createClientConfiguration(WebClientFactory webClientFactory,
                                               BaseWebClientProps baseWebClientProps) {
        return webClientFactory.getInstance(baseWebClientProps);
    }

    @Autowired
    @Bean("override")
    public WebClient createClientConfiguration(WebClientFactory webClientFactory,
                                               OverrideWebClientProps overrideWebClientProps) {
        return webClientFactory.getInstance(overrideWebClientProps);
    }
}
