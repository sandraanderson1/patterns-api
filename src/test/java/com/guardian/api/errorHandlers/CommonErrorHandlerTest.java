package com.guardian.api.errorHandlers;

import com.guardian.Downstream;
import com.guardian.api.exceptions.UnsupportedOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CommonErrorHandlerTest<T> {

    @Mock
    private ClientResponse clientResponse;

    private ErrorHandler<T> createCommonErrorHandler() {
        return new CommonErrorHandler<>(new GuardianErrorHandler<>(null));
    }

    @BeforeEach
    public void setUp() {
        clientResponse = mock(ClientResponse.class);
    }

    @Test
    void handleError_returnsServerErrorExceptionIfDownstreamResponds5xx() {
        given(clientResponse.statusCode()).willReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        StepVerifier.create(createCommonErrorHandler().handleError(clientResponse, Downstream.GUARDIAN))
                .expectError(HttpServerErrorException.class)
                .verify();
    }

    @Test
    void handleError_returnsClientExceptionIfDownstreamResponds4xx() {
        given(clientResponse.statusCode()).willReturn(HttpStatus.BAD_REQUEST);

        StepVerifier.create(createCommonErrorHandler().handleError(clientResponse, Downstream.GUARDIAN))
                .expectError(HttpClientErrorException.class)
                .verify();
    }

    @Test
    void handleError_returnsUnsupportedOperationIfDownstreamResponds303() {
        given(clientResponse.statusCode()).willReturn(HttpStatus.SEE_OTHER);

        StepVerifier.create(createCommonErrorHandler().handleError(clientResponse, Downstream.GUARDIAN))
                .expectError(UnsupportedOperationException.class)
                .verify();
    }
}