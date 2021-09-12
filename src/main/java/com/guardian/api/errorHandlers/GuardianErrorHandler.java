package com.guardian.api.errorHandlers;

import com.guardian.Downstream;
import com.guardian.api.exceptions.ExceptionsFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class GuardianErrorHandler<T> extends ErrorHandler<T>{

    public GuardianErrorHandler(ErrorHandler<T> nextErrorHandler) {
        super(nextErrorHandler);
    }

    @Override
    public Mono<T> handleError(ClientResponse response, Downstream downstream) {
        logDownstreamError(response, downstream);
        return response.statusCode().equals(HttpStatus.SEE_OTHER) ?
                Mono.error(ExceptionsFactory.createSpecialException(downstream)) : response.createException().flatMap(Mono::error);
    }
}
