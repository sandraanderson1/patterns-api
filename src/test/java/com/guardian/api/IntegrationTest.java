package com.guardian.api;

import com.guardian.ApiApplication;
import com.guardian.api.response.guardian.GuardianResponse;
import com.guardian.api.services.GuardianService;
import com.guardian.api.strategy.GuardianStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    public void guardianHandler_returnsSuccessfulGuardianResponse() {

        testClient.get()
                .uri("/guardian")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(GuardianResponse.class)
                .returnResult();
    }

    @Test
    public void guardianHandler_returnsBadRequestIfClientErrorOccurs() {
        given(strategy.callDownstream()).willThrow(create(HttpStatus.BAD_REQUEST, null, null, null, null));

        testClient.get()
                .uri("/guardian")
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
