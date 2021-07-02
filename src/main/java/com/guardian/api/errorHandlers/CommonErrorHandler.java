package com.guardian.api.errorHandlers;

import com.guardian.Downstream;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static com.guardian.ExceptionsFactory.createHttpClientException;
import static com.guardian.ExceptionsFactory.createHttpServerException;

@Component
public class CommonErrorHandler <T> {

    public Mono<T> handleDownstreamError(Throwable throwable, Downstream downstream) {
        if (throwable instanceof WebClientResponseException) {
            WebClientResponseException error = (WebClientResponseException) throwable;

            if (error.getStatusCode().is5xxServerError()) {
                return Mono.error(createHttpServerException(downstream));
            } else if (error.getStatusCode().is4xxClientError()) {
                return Mono.error(createHttpClientException(downstream));
            } else {
                return Mono.error(new RuntimeException("oopps something defo went wrong!"));
            }
        }
        return null;
    }
}
