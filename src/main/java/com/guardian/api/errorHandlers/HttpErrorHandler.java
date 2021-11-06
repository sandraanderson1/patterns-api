package com.guardian.api.errorHandlers;

import com.guardian.Downstream;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public interface HttpErrorHandler {
    <T> Mono<T> handleError(ClientResponse clientResponse, Downstream downstream);
    String getName();
}
