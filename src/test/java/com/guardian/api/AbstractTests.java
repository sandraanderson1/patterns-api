package com.guardian.api;

import com.guardian.api.webClient.BaseWebClientProps;
import com.guardian.api.webClient.OverrideWebClientProps;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {ApplicationProps.class, BaseWebClientProps.class, OverrideWebClientProps.class})
@ActiveProfiles("test")
public abstract class AbstractTests {

    public static MockWebServer mockWebServer;
    public static MockResponse mockResponse;

    @Mock
    private ApplicationProps applicationProps;

    @BeforeAll
    public static void initialiseWebServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    public void setUp() throws IOException {

        String baseUrl = mockWebServer.url("/").toString();
        given(applicationProps.getBaseUrl()).willReturn(baseUrl);

        mockResponse = new MockResponse();
        mockResponse.addHeader("Content-Type", "application/json; charset=utf-8");
        mockResponse.setBody(mockResponse());

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
