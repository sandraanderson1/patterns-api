package com.guardian.api.errorHandlers;

import com.guardian.Downstream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Slf4j
public abstract class ErrorHandler implements HttpErrorHandler {

    public ErrorHandler nextErrorHandler;

    public ErrorHandler(ErrorHandler nextErrorHandler) {
        this.nextErrorHandler = nextErrorHandler;
    }

    @Override
    public abstract <T> Mono<T> handleError(ClientResponse response, Downstream downstream);

    public void logDownstreamError(ClientResponse response, Downstream downstream) {
        log.error("Reported error from Downstream: {}, {}", downstream, response.statusCode());
    }
}
