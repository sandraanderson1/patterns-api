package com.guardian.api.services;

import com.guardian.Downstream;
import com.guardian.api.DownstreamExecutor;
import com.guardian.api.errorHandlers.ErrorHandler;
import com.guardian.api.properties.GuardianProps;
import com.guardian.api.errorHandlers.CommonErrorHandler;
import com.guardian.api.errorHandlers.GuardianErrorHandler;
import com.guardian.api.mappers.GuardianToClientResponseMapper;
import com.guardian.api.response.guardian.GuardianClientResponse;
import com.guardian.api.response.guardian.GuardianResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GuardianService implements NewsService<GuardianClientResponse> {

    private final GuardianProps guardianProps;
    private final GuardianToClientResponseMapper guardianToClientResponseMapper;
    private final WebClient webClient;
    private final ErrorHandler errorHandler;
    private final DownstreamExecutor<GuardianResponse> guardianDownstreamExecutor;

    public GuardianService(GuardianProps guardianProps,
                           GuardianToClientResponseMapper guardianToClientResponseMapper,
                           @Qualifier("base") WebClient webClient,
                           @Qualifier("guardianExecutorConfig") DownstreamExecutor<GuardianResponse> guardianDownstreamExecutor) {
        this.guardianProps = guardianProps;
        this.guardianToClientResponseMapper = guardianToClientResponseMapper;
        this.webClient = webClient;
        this.guardianDownstreamExecutor = guardianDownstreamExecutor;
        this.errorHandler = new CommonErrorHandler(new GuardianErrorHandler(null));
    }

    @Override
    public Mono<GuardianClientResponse> getNews() {
        System.out.println("From service: " + errorHandler.getName());
        return guardianDownstreamExecutor.execute(webClient, guardianProps, errorHandler, Downstream.GUARDIAN)
                .map(guardianToClientResponseMapper)
                .doOnError(error -> log.error("API returning error: {}", error.getMessage()));
    }
}
