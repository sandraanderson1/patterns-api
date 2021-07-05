package com.guardian.api.services;

import com.guardian.Downstream;
import com.guardian.api.GuardianProps;
import com.guardian.api.mappers.GuardianToClientResponseMapper;
import com.guardian.api.response.guardian.GuardianClientResponse;
import com.guardian.api.response.guardian.GuardianResponse;
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
public class GuardianService {

    private final GuardianProps guardianProps;
    private final GuardianToClientResponseMapper guardianToClientResponseMapper;
    private final WebClient webClient;

    public GuardianService(GuardianProps guardianProps, GuardianToClientResponseMapper guardianToClientResponseMapper,
                           @Qualifier("base") WebClient webClient) {
        this.guardianProps = guardianProps;
        this.guardianToClientResponseMapper = guardianToClientResponseMapper;
        this.webClient = webClient;
    }

    public Mono<GuardianClientResponse> read() {
        return webClient
                .get()
                .uri(guardianProps.getBaseUrl() + guardianProps.getApiKey())
                .exchangeToMono(this::handleResponse)
                .map(guardianToClientResponseMapper)
                .doOnError(error -> log.error("Reported error: {}, {}", error.getMessage(), error.getStackTrace()))
                .onErrorResume(throwable -> Mono.error(createHttpServerException(Downstream.GUARDIAN)));
    }

    //not working with global exceeption handler
    private Mono<GuardianResponse> handleResponse(ClientResponse response) {
        if (response.statusCode().equals(HttpStatus.OK)) {
            return response.bodyToMono(GuardianResponse.class);
        } else if (response.statusCode().is4xxClientError()) {
            return Mono.error(createHttpClientException(Downstream.GUARDIAN));
        } else if (response.statusCode().is5xxServerError()) {
            return Mono.error(createHttpServerException(Downstream.GUARDIAN));
        } else {
            return response.createException().flatMap(Mono::error);
        }
    }

    //exchangeToMono - if you care about the status code - return type will be ? extends throwable
    //look at DownstreamExecutor
    //onErrorResume have default exception that always pops up

    //strategy pattern  + chain responsibility pattern
    //interface ie. callDownstream -> strategy that delegates to Gueardian or One
    //have 2 different strategies - guardian + something else (another api) + header
    //decorator for each api calls made
    // 2 error handlers handing error responses from downstream ie. some error code will be handled differently between downstreams
    // eg. 1 handler handles only 5xx, the other both
    //look at SynacorStartTrackingHttpClientErrorHandler (calling to AbstractClass) -> not decorator pattern but similar idea


    //composition based stuff


//    https://github.com/matarrese/content-api-the-guardian - maybe use this?
}
