package com.guardian.api.services;

import com.guardian.Downstream;
import com.guardian.api.mappers.GuardianToClientResponseMapper;
import com.guardian.api.response.ClientResponse;
import com.guardian.api.response.guardian.GuardianResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static com.guardian.ExceptionsFactory.createHttpClientException;
import static com.guardian.ExceptionsFactory.createHttpServerException;

@Slf4j
@Service
public class GuardianService {

    private final GuardianToClientResponseMapper guardianToClientResponseMapper;
    private final WebClient webClient;

    public GuardianService(GuardianToClientResponseMapper guardianToClientResponseMapper,
                           @Qualifier("base") WebClient webClient) {
        this.guardianToClientResponseMapper = guardianToClientResponseMapper;
        this.webClient = webClient;
    }

    public Mono<ClientResponse> read() {
        return webClient
                .get()
                .retrieve()
                .bodyToMono(GuardianResponse.class)
                .map(guardianToClientResponseMapper)
                .doOnError(error -> log.error("Reported error: {}, {}", error.getMessage(), error.getStackTrace()))
                .onErrorResume(throwable -> {
                    Mono<ClientResponse> response = handleDownstreamError(throwable);
                    if (response != null) return response;
                    return Mono.error(throwable);
                });
    }

    //configuration file to inject the factory there - inject webclient bean - DownstreamHttpActionConfiguration
    //strategy pattern  + decorator pattern
    //have 2 different strategies - guardian + something else (another api) + header
    //decorator for each api calls made, 2 error handlers handing error responses from downstream ie. some error codde will be handled differently between downstreams
    //composition based stuff

    private Mono<ClientResponse> handleDownstreamError(Throwable throwable) {
        if (throwable instanceof WebClientResponseException) {
            WebClientResponseException error = (WebClientResponseException) throwable;

            if (error.getStatusCode().is5xxServerError()) {
                return Mono.error(createHttpServerException(Downstream.GUARDIAN));
            } else if (error.getStatusCode().is4xxClientError()) {
                return Mono.error(createHttpClientException(Downstream.GUARDIAN));
            } else {
                return Mono.error(new RuntimeException("oopps something defo went wrong!"));
            }
        }
        return null;
    }
}
