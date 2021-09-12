package com.guardian.api;

import com.guardian.api.exceptions.UnsupportedOperationException;
import com.guardian.api.mappers.GuardianToClientResponseMapper;
import com.guardian.api.services.GuardianService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@ExtendWith(MockitoExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = {GuardianProps.class})
@ActiveProfiles("test")
class GuardianServiceTest {

    private static MockWebServer mockWebServer;
    private static MockResponse mockResponse;
    @Autowired
    private GuardianProps guardianProps;
    private GuardianService service;

    @BeforeAll
    public static void initialiseWebServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    public void setUp() throws IOException {
        service = new GuardianService(guardianProps, new GuardianToClientResponseMapper(), WebClient.create(mockWebServer.url("/").toString()));
        mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8");
        mockResponse.setBody(mockResponse());
    }

    @Test
    void read_returnsClientResponseMono() {
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(service.read())
                .expectNextMatches(response -> response.getUserData().getUserTier().equals("developer"))
                .verifyComplete();
    }

    @Test
    void read_returnsServerErrorExceptionIfDownstreamResponds5xx() {
        mockResponse.setResponseCode(500);
        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(service.read())
                .expectError(HttpServerErrorException.class)
                .verify();
    }

    @Ignore
    @Test
    void read_returnsClientErrorExceptionIfDownstreamResponds4xx() {
        mockResponse.setResponseCode(404);
        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(service.read())
                .expectError(HttpClientErrorException.class)
                .verify();
    }

    @Test
    void read_returnsServerErrorExceptionIfForNonSpecificHttpCodes() {
        mockResponse.setResponseCode(303);
        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(service.read())
                .expectError(UnsupportedOperationException.class)
                .verify();
    }

    private String mockResponse() throws IOException {
        String file = "src/test/resources/response.json";
        String response = new String(Files.readAllBytes(Paths.get(file)));
        return response;
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}
