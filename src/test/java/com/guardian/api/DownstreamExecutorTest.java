package com.guardian.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardian.api.errorHandlers.CommonErrorHandler;
import com.guardian.api.errorHandlers.GuardianErrorHandler;
import com.guardian.api.exceptions.UnsupportedOperationException;
import com.guardian.api.properties.GuardianProps;
import com.guardian.api.properties.OneProps;
import com.guardian.api.response.guardian.GuardianResponse;
import com.guardian.api.response.one.OneResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Paths;

import static com.guardian.Downstream.GUARDIAN;
import static com.guardian.Downstream.ONE;
import static com.guardian.api.exceptions.ExceptionsFactory.*;
import static java.nio.file.Files.readAllBytes;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = {GuardianProps.class, OneProps.class})
@ActiveProfiles("test")
class DownstreamExecutorTest {

    private static MockWebServer mockWebServer;
    private static MockResponse mockResponse;
    private static WebClient webClient;
    private static DownstreamExecutor<GuardianResponse> guardianDownstreamExecutor;
    private static DownstreamExecutor<OneResponse> oneDownstreamExecutor;
    private static ObjectMapper mapper;

    @Autowired
    private GuardianProps guardianProps;
    @Autowired
    private OneProps oneProps;
    @Mock
    private CommonErrorHandler commonErrorHandler;
    @Mock
    private GuardianErrorHandler guardianErrorHandler;

    @BeforeAll
    public static void initialiseWebServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    public void setUp() {
        Hooks.onOperatorDebug();
        guardianDownstreamExecutor = new DownstreamExecutor<>(GuardianResponse.class);
        oneDownstreamExecutor = new DownstreamExecutor<>(OneResponse.class);
        webClient = WebClient.create(mockWebServer.url("/").toString());
        mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8");
        mapper = new ObjectMapper();
    }

    @Test
    void execute_returnsSuccessfulGuardianClientResponse() throws IOException {
        mockResponse.setBody(mockGuardianResponse());
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(guardianDownstreamExecutor.execute(webClient, guardianProps, commonErrorHandler, GUARDIAN))
                .expectNextMatches(response -> response.getResponse().getUserTier().equals("developer"))
                .verifyComplete();
    }

    @Test
    void execute_returnsSuccessfulOneClientResponse() throws JsonProcessingException {
        mockResponse.setBody((mockOneResponse()));
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(oneDownstreamExecutor.execute(webClient, oneProps, commonErrorHandler, ONE))
                .expectNextMatches(response -> response.getAffirmation().equals("response"))
                .verifyComplete();
    }

    @Test
    void execute_returnsClientErrorOn4xxResponse() {
        mockResponse.setResponseCode(400);
        mockWebServer.enqueue(mockResponse);

        given(commonErrorHandler.handleError(any(), eq(GUARDIAN))).willReturn(Mono.error(createHttpClientException(GUARDIAN)));

        StepVerifier.create(guardianDownstreamExecutor.execute(webClient, guardianProps, commonErrorHandler, GUARDIAN))
                .expectErrorMatches(throwable -> throwable instanceof HttpClientErrorException &&
                        throwable.getMessage().equals("400 Downstream GUARDIAN has failed"))
                .verify();
    }

    @Test
    void execute_returnsServerErrorOn5xxResponse() {
        mockResponse.setResponseCode(504);
        mockWebServer.enqueue(mockResponse);

        given(commonErrorHandler.handleError(any(), eq(ONE))).willReturn(Mono.error(createHttpServerException(ONE)));

        StepVerifier.create(oneDownstreamExecutor.execute(webClient, oneProps, commonErrorHandler, ONE))
                .expectErrorMatches(throwable -> throwable instanceof HttpServerErrorException &&
                        throwable.getMessage().equals("500 Downstream ONE has failed"))
                .verify();
    }

    @Test
    void execute_returnsUnsupportedOperationOn303ResponseForGuardianDownstream() {
        mockResponse.setResponseCode(303);
        mockWebServer.enqueue(mockResponse);

        given(guardianErrorHandler.handleError(any(), eq(GUARDIAN))).willReturn(Mono.error(createSpecialException(GUARDIAN)));

        StepVerifier.create(guardianDownstreamExecutor.execute(webClient, guardianProps, guardianErrorHandler, GUARDIAN))
                .expectErrorMatches(throwable -> throwable instanceof UnsupportedOperationException &&
                        throwable.getMessage().equals("Unsupported operation for API"))
                .verify();
    }

    @Test
    void execute_returnsInternalServerErrorOn303ResponseForOneDownstream() {
        mockResponse.setResponseCode(303);
        mockWebServer.enqueue(mockResponse);

        given(commonErrorHandler.handleError(any(), eq(ONE))).willReturn(Mono.error(createHttpServerException(ONE)));

        StepVerifier.create(oneDownstreamExecutor.execute(webClient, oneProps, commonErrorHandler, ONE))
                .expectErrorMatches(throwable -> throwable instanceof HttpServerErrorException &&
                        throwable.getMessage().equals("500 Downstream ONE has failed"))
                .verify();
    }

    private String mockGuardianResponse() throws IOException {
        return new String(readAllBytes(Paths.get("src/test/resources/response.json")));
    }

    private String mockOneResponse() throws JsonProcessingException {
        return mapper.writeValueAsString("response");
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}