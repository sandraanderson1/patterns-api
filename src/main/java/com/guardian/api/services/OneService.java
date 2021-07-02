package com.guardian.api.services;

import com.guardian.Downstream;
import com.guardian.api.OneProps;
import com.guardian.api.errorHandlers.CommonErrorHandler;
import com.guardian.api.response.one.OneResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class OneService {

    private final OneProps oneProps;
    private final WebClient webClient;
    private final CommonErrorHandler errorHandler;

    public OneService(OneProps oneProps,
                      @Qualifier("override") WebClient webClient, CommonErrorHandler errorHandler) {
        this.oneProps = oneProps;
        this.webClient = webClient;
        this.errorHandler = errorHandler;
    }

    public Mono<OneResponse> read() {
        return webClient
                .get()
                .uri(oneProps.getBaseUrl())
                .retrieve()
                .bodyToMono(OneResponse.class)
                .doOnError(error -> log.error("Reported error: {}, {}", error.getMessage(), error.getStackTrace()))
                .onErrorResume(throwable -> {
                    Mono response = errorHandler.handleDownstreamError(throwable, Downstream.ONE);
                    if (response != null) return response;
                    return Mono.error(throwable);
                });

        //error handler not working
    }
}
