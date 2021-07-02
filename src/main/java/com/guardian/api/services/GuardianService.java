package com.guardian.api.services;

import com.guardian.Downstream;
import com.guardian.api.GuardianProps;
import com.guardian.api.errorHandlers.CommonErrorHandler;
import com.guardian.api.mappers.GuardianToClientResponseMapper;
import com.guardian.api.response.guardian.ClientResponse;
import com.guardian.api.response.guardian.GuardianResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GuardianService {

    private final GuardianProps guardianProps;
    private final GuardianToClientResponseMapper guardianToClientResponseMapper;
    private final WebClient webClient;
    private final CommonErrorHandler errorHandler;

    public GuardianService(GuardianProps guardianProps, GuardianToClientResponseMapper guardianToClientResponseMapper,
                           @Qualifier("base") WebClient webClient, CommonErrorHandler errorHandler) {
        this.guardianProps = guardianProps;
        this.guardianToClientResponseMapper = guardianToClientResponseMapper;
        this.webClient = webClient;
        this.errorHandler = errorHandler;
    }

    public Mono<ClientResponse> read() {
        return webClient
                .get()
                .uri(guardianProps.getBaseUrl() + guardianProps.getApiKey())
                .retrieve()
                .bodyToMono(GuardianResponse.class)
                .map(guardianToClientResponseMapper)
                .doOnError(error -> log.error("Reported error: {}, {}", error.getMessage(), error.getStackTrace()))
                .onErrorResume(throwable -> {
                    Mono response = errorHandler.handleDownstreamError(throwable, Downstream.GUARDIAN);
                    if (response != null) return response;
                    return Mono.error(throwable);
                });
    }

    //configuration file to inject the factory there - inject webclient bean - DownstreamHttpActionConfiguration
    //strategy pattern  + decorator pattern
    //have 2 different strategies - guardian + something else (another api) + header
    //decorator for each api calls made, 2 error handlers handing error responses from downstream ie. some error codde will be handled differently between downstreams
    //composition based stuff

}
