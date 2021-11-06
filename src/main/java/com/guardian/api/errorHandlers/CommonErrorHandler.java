package com.guardian.api.errorHandlers;

import com.guardian.Downstream;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import static com.guardian.api.exceptions.ExceptionsFactory.createHttpClientException;
import static com.guardian.api.exceptions.ExceptionsFactory.createHttpServerException;

public class CommonErrorHandler extends ErrorHandler {

    public CommonErrorHandler(ErrorHandler nextErrorHandler) {
        super(nextErrorHandler);
    }

    @Override
    public <T> Mono<T> handleError(ClientResponse response, Downstream downstream) {
        if (response.statusCode().is4xxClientError()) {
            logDownstreamError(response, downstream);
            return Mono.error(createHttpClientException(downstream));
        } else if (response.statusCode().is5xxServerError()) {
            logDownstreamError(response, downstream);
            return Mono.error(createHttpServerException(downstream));
        } else if (nextErrorHandler != null) {
            logDownstreamError(response, downstream);
            return nextErrorHandler.handleError(response, downstream);
        }
        return Mono.empty();
    }

    @Override
    public String getName() {
        return "Common";
    }

    // 2 error handlers handing error responses from downstream ie. some error code will be handled differently between downstreams
}
