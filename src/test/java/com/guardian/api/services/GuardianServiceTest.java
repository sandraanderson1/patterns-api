package com.guardian.api.services;

import com.guardian.Downstream;
import com.guardian.api.DownstreamExecutor;
import com.guardian.api.dataModels.UserData;
import com.guardian.api.mappers.GuardianToClientResponseMapper;
import com.guardian.api.properties.GuardianProps;
import com.guardian.api.response.guardian.GuardianArticle;
import com.guardian.api.response.guardian.GuardianClientResponse;
import com.guardian.api.response.guardian.GuardianResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

import static com.guardian.api.ApplicationTests.mockGuardianResponseObj;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = {GuardianProps.class})
@ActiveProfiles("test")
class GuardianServiceTest {

    private GuardianService service;

    @Mock
    private DownstreamExecutor<GuardianResponse> guardianDownstreamExecutor;
    @Mock
    private WebClient webClient;
    @Autowired
    private GuardianProps guardianProps;
    @Mock
    private GuardianToClientResponseMapper mapper;

    @BeforeEach
    void setUp() {
        service = new GuardianService(guardianProps, mapper, webClient, guardianDownstreamExecutor);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void getNews_returnsSuccessfulResponse() throws IOException {
        List<GuardianArticle> guardianArticles = List.of(new GuardianArticle());
        GuardianResponse guardianResponse = mockGuardianResponseObj();
        GuardianClientResponse guardianClientResponse = new GuardianClientResponse(new UserData.Builder("developer").withUserName("Bob").build(), "ok", guardianArticles);

        given(guardianDownstreamExecutor.execute(eq(webClient), eq(guardianProps), any(), eq(Downstream.GUARDIAN)))
                .willReturn(Mono.just(guardianResponse));
        given(mapper.apply(guardianResponse)).willReturn(guardianClientResponse);

        StepVerifier.create(service.getNews())
                .expectNextMatches(response -> response.getStatus().equals("ok"))
                .verifyComplete();
    }
}