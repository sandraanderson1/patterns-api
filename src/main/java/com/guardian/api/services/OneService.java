package com.guardian.api.services;

import com.guardian.Downstream;
import com.guardian.api.DownstreamExecutor;
import com.guardian.api.mappers.OneToClientResponseMapper;
import com.guardian.api.properties.OneProps;
import com.guardian.api.errorHandlers.CommonErrorHandler;
import com.guardian.api.response.one.OneClientResponse;
import com.guardian.api.response.one.OneResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class OneService implements NewsService<OneClientResponse> {

    private final OneProps oneProps;
    private final OneToClientResponseMapper oneToClientResponseMapper;
    private final WebClient webClient;
    private final CommonErrorHandler errorHandler;
    private final DownstreamExecutor<OneResponse> oneDownstreamExecutor;

    public OneService(OneProps oneProps,
                      OneToClientResponseMapper oneToClientResponseMapper,
                      @Qualifier("override") WebClient webClient,
                      @Qualifier("oneExecutorConfig") DownstreamExecutor<OneResponse> oneDownstreamExecutor) {
        this.oneProps = oneProps;
        this.oneToClientResponseMapper = oneToClientResponseMapper;
        this.webClient = webClient;
        this.oneDownstreamExecutor = oneDownstreamExecutor;
        this.errorHandler = new CommonErrorHandler(null);
    }

    @Override
    public Mono<OneClientResponse> getNews() {
        return oneDownstreamExecutor.execute(webClient, oneProps, errorHandler, Downstream.ONE)
                .map(oneToClientResponseMapper);
//                .doOnError(error -> log.error("API returning error: {}", error.getMessage()));
    }
}
