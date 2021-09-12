package com.guardian.api.services;

import com.guardian.Downstream;
import com.guardian.api.GuardianProps;
import com.guardian.api.errorHandlers.CommonErrorHandler;
import com.guardian.api.errorHandlers.GuardianErrorHandler;
import com.guardian.api.mappers.GuardianToClientResponseMapper;
import com.guardian.api.response.guardian.GuardianClientResponse;
import com.guardian.api.response.guardian.GuardianResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GuardianService {

    private final GuardianProps guardianProps;
    private final GuardianToClientResponseMapper guardianToClientResponseMapper;
    private final WebClient webClient;
    private final CommonErrorHandler<GuardianResponse> errorHandler;

    public GuardianService(GuardianProps guardianProps, GuardianToClientResponseMapper guardianToClientResponseMapper,
                           @Qualifier("base") WebClient webClient) {
        this.guardianProps = guardianProps;
        this.guardianToClientResponseMapper = guardianToClientResponseMapper;
        this.webClient = webClient;
        this.errorHandler = new CommonErrorHandler<GuardianResponse>(new GuardianErrorHandler<>(null));
    }

//consider context for errors so that we don't lose the original status code
    public Mono<GuardianClientResponse> read() {
        return webClient
                .get()
                .uri(guardianProps.getBaseUrl() + guardianProps.getApiKey())
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(GuardianResponse.class);
                    } else {
                        return errorHandler.handleError(response, Downstream.GUARDIAN);
                    }
                })
                .map(guardianToClientResponseMapper)
                .doOnError(error -> log.error("API returning error: {}", error.getMessage()));
//                this is masking all earlier errors?
//                .onErrorResume(throwable -> Mono.error(createHttpServerException(Downstream.GUARDIAN)));
    }
}
