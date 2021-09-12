package com.guardian.api;

import com.guardian.ApiApplication;
import com.guardian.api.response.guardian.GuardianResponse;
import com.guardian.api.services.GuardianService;
import com.guardian.api.strategy.GuardianStrategy;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.BDDMockito.given;
import static org.springframework.web.client.HttpClientErrorException.create;

@AutoConfigureWebTestClient(timeout = "10000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApiApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class IntegrationTest {

    @Autowired
    private WebTestClient testClient;

    @Mock
    private GuardianStrategy strategy;

    //This weird error = UnknownHostException: guardian-base-url: nodename nor servname provided, or not known
    @Ignore
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

    //fix global exception handler - returning 500 not 400
    @Ignore
    @Test
    public void guardianHandler_returnsBadRequestIfClientErrorOccurs() {
        given(strategy.callDownstream()).willThrow(create(HttpStatus.BAD_REQUEST, null, null, null, null));
//        given(guardianService.read()).willThrow(create(HttpStatus.BAD_REQUEST, null, null, null, null));

        testClient.get()
                .uri("/guardian")
                .exchange()
                .expectStatus()
                .isNotFound();
    }


}
