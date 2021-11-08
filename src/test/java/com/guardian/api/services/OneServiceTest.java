package com.guardian.api.services;

import com.guardian.Downstream;
import com.guardian.api.DownstreamExecutor;
import com.guardian.api.mappers.OneToClientResponseMapper;
import com.guardian.api.properties.OneProps;
import com.guardian.api.response.one.OneClientResponse;
import com.guardian.api.response.one.OneResponse;
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

import static com.guardian.api.ApplicationTests.mockOneResponseObj;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = {OneProps.class})
@ActiveProfiles("test")
class OneServiceTest {

    private OneService service;

    @Mock
    private DownstreamExecutor<OneResponse> oneDownstreamExecutor;
    @Mock
    private WebClient webClient;
    @Autowired
    private OneProps oneProps;
    @Mock
    private OneToClientResponseMapper mapper;

    @BeforeEach
    void setUp() {
        service = new OneService(oneProps, mapper, webClient, oneDownstreamExecutor);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void getNews_returnsSuccessfulResponse() throws IOException {
        OneResponse oneResponse = mockOneResponseObj();
        OneClientResponse oneClientResponse = new OneClientResponse("oneResponse");

        given(oneDownstreamExecutor.execute(eq(webClient), eq(oneProps), any(), eq(Downstream.ONE)))
                .willReturn(Mono.just(oneResponse));
        given(mapper.apply(oneResponse)).willReturn(oneClientResponse);

        StepVerifier.create(service.getNews())
                .expectNextMatches(response -> response.getResult().equals("oneResponse"))
                .verifyComplete();
    }
}