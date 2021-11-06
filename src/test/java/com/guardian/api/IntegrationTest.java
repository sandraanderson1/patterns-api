package com.guardian.api;

import com.guardian.ApiApplication;
import com.guardian.api.response.guardian.GuardianResponse;
import com.guardian.api.response.one.OneResponse;
import com.guardian.api.services.GuardianService;
import com.guardian.api.strategy.GuardianStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.BDDMockito.given;
import static org.springframework.web.client.HttpClientErrorException.create;

@AutoConfigureWebTestClient(timeout = "10000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApiApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class IntegrationTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private GuardianStrategy strategy;
    @MockBean
    private GuardianService service;

    @Test
    public void handler_returnsSuccessfulGuardianResponse_whenLanguageAcceptHeaderProvided() {
        testClient.get()
                .uri("/news")
                .header("Accept-Language", "en-GB")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(GuardianResponse.class)
                .returnResult();
    }

    @Test
    public void handler_returnsSuccessfulOneResponse_whenLanguageAcceptHeaderNotProvided() {
        testClient.get()
                .uri("/news")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(OneResponse.class)
                .returnResult();
    }

    @Test
    public void handler_returnsSuccessfulOneResponse_whenNotRecognisedLanguageRangeProvided() {
        testClient.get()
                .uri("/news")
                .header("Accept-Language", "en-AU")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(OneResponse.class)
                .returnResult();
    }

    @Test
    public void guardianHandler_returnsBadRequestIfClientErrorOccurs() {
        given(strategy.callDownstream()).willThrow(create(HttpStatus.BAD_REQUEST, "status", HttpHeaders.EMPTY, null, null));

        testClient.get()
                .uri("/news")
                .header("Accept-Language", "en-GB")
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
