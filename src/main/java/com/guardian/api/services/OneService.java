package com.guardian.api.services;

import com.guardian.Downstream;
import com.guardian.api.OneProps;
import com.guardian.api.response.guardian.GuardianResponse;
import com.guardian.api.response.one.OneResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.guardian.ExceptionsFactory.createHttpClientException;
import static com.guardian.ExceptionsFactory.createHttpServerException;

@Slf4j
@Service
public class OneService {

    private final OneProps oneProps;
    private final WebClient webClient;

    public OneService(OneProps oneProps,
                      @Qualifier("override") WebClient webClient) {
        this.oneProps = oneProps;
        this.webClient = webClient;
    }

    public Mono<OneResponse> read() {
        return webClient
                .get()
                .uri(oneProps.getBaseUrl())
                .exchangeToMono(this::handleResponse)
                .doOnError(error -> log.error("Reported error: {}", error.getMessage()));
    }

    private Mono<OneResponse> handleResponse(ClientResponse response) {
        if (response.statusCode().equals(HttpStatus.OK)) {
            return response.bodyToMono(OneResponse.class);
        } else if (response.statusCode().is4xxClientError()) {
            logDownstreamError(response);
            return Mono.error(createHttpClientException(Downstream.ONE));
        } else if (response.statusCode().is5xxServerError()) {
            logDownstreamError(response);
            return Mono.error(createHttpServerException(Downstream.ONE));
        } else {
            logDownstreamError(response);
            return response.createException().flatMap(Mono::error);
        }
    }

    private void logDownstreamError(ClientResponse response) {
        log.error("Reported error from Downstream: {}, {}", Downstream.ONE, response.statusCode());
    }
}
