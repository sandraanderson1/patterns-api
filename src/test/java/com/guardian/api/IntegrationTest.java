package com.guardian.api;

import com.guardian.ApiApplication;
import com.guardian.api.response.guardian.GuardianResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient(timeout = "10000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApiApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class IntegrationTest {

    @Autowired
    private WebTestClient testClient;

    @Test
    public void getNews_returnsSuccecssfulGuardianResponse() {

        testClient.get()
                .uri("/guardian")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(GuardianResponse.class)
                .returnResult();
    }


}
