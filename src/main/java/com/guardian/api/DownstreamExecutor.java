package com.guardian.api;

import com.guardian.Downstream;
import com.guardian.api.errorHandlers.HttpErrorHandler;
import com.guardian.api.properties.DownstreamProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class DownstreamExecutor<DownstreamResponse> {

    private final Class<DownstreamResponse> downstreamResponseType;

    public DownstreamExecutor(Class<DownstreamResponse> downstreamResponseType) {
        this.downstreamResponseType = downstreamResponseType;
    }

    public Mono<DownstreamResponse> execute(WebClient webClient,
                                                      DownstreamProps downstreamProps,
                                                      HttpErrorHandler errorHandler,
                                                      Downstream downstream) {
        return webClient
                .get()
                .uri(downstreamProps.createDownstreamUrl())
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(downstreamResponseType);
                    } else {
                        return errorHandler.handleError(response, downstream);
                    }
                })
                .log()
                .doOnEach(r -> log.info("what's this " + r.get()));
    }
}
