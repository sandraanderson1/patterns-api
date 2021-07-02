package com.guardian.api.webClient;

import com.guardian.api.GuardianProps;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class WebClientFactory {

    private final GuardianProps applicationProps;

    public WebClientFactory(GuardianProps applicationProps) {
        this.applicationProps = applicationProps;
    }

    public WebClient getInstance(WebClientProps webClientProps) {

        HttpClient httpClient = HttpClient.create()
                .resolver(DefaultAddressResolverGroup.INSTANCE)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webClientProps.getConnectTimeoutMs())
                .responseTimeout(Duration.ofMillis(webClientProps.getTotalRequestTimeout()))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(webClientProps.getReadTimeoutMs(), TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(webClientProps.getReadTimeoutMs(), TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
