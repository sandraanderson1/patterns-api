package com.guardian.api.errorHandlers;

import com.guardian.Downstream;
import com.guardian.api.exceptions.ExceptionsFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Slf4j
public class GuardianErrorHandler extends ErrorHandler {

    public GuardianErrorHandler(ErrorHandler nextErrorHandler) {
        super(nextErrorHandler);
    }

    @Override
    public <T> Mono<T> handleError(ClientResponse response, Downstream downstream) {
        if (response.statusCode().equals(HttpStatus.SEE_OTHER)) {
            logDownstreamError(response, downstream);
            return Mono.error(ExceptionsFactory.createSpecialException(downstream));
        } else {
            logDownstreamError(response, downstream);
            return response.createException().flatMap(Mono::error);
        }
    }

    @Override
    public String getName() {
        return "Guardian";
    }

    //have a generic error handler to manage the default errors .. not relevant to Guardian
    //maybe swap order GuardianFrist then delegate to the common one to have the default stuff ..
    //chain what we want to run first
}
