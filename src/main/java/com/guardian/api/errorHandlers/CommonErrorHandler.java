package com.guardian.api.errorHandlers;

import com.guardian.Downstream;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import static com.guardian.api.exceptions.ExceptionsFactory.createHttpClientException;
import static com.guardian.api.exceptions.ExceptionsFactory.createHttpServerException;

public class CommonErrorHandler<T> extends ErrorHandler<T> {

    public CommonErrorHandler(ErrorHandler<T> nextErrorHandler) {
        super(nextErrorHandler);
    }

    @Override
    public Mono<T> handleError(ClientResponse response, Downstream downstream) {
        if (response.statusCode().is4xxClientError()) {
            logDownstreamError(response, downstream);
            return Mono.error(createHttpClientException(downstream));
        } else if (response.statusCode().is5xxServerError()) {
            logDownstreamError(response, downstream);
            return Mono.error(createHttpServerException(downstream));
        } else if (nextErrorHandler != null) {
            return nextErrorHandler.handleError(response, downstream);
        }
        return Mono.empty();
    }

    //have 2 different strategies - guardian + something else (another api) + header // DONE
    //decorator for each api calls made
    // 2 error handlers handing error responses from downstream ie. some error code will be handled differently between downstreams
}
