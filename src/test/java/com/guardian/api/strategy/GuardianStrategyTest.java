package com.guardian.api.strategy;

import com.guardian.api.dataModels.UserData;
import com.guardian.api.response.guardian.GuardianArticle;
import com.guardian.api.response.guardian.GuardianClientResponse;
import com.guardian.api.services.GuardianService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GuardianStrategyTest {

    private static final GuardianClientResponse GUARDIAN_CLIENT_RESPONSE = new GuardianClientResponse(
            new UserData.Builder("tier").build(), "status", List.of(new GuardianArticle()));

    private GuardianStrategy strategy;
    @Mock
    private GuardianService service;

    @BeforeEach
    public void setUp() {
        strategy = new GuardianStrategy(service);
    }

    @Test
    public void callDownstream_returnsGuardianClientResponse() {
        given(service.read()).willReturn(Mono.just(GUARDIAN_CLIENT_RESPONSE));
        StepVerifier.create(strategy.callDownstream())
                .expectNext(GUARDIAN_CLIENT_RESPONSE)
                .verifyComplete();
    }

    //TODO should there be a case where we get an empty response from the service? If there was an issue, an error would throw
//    @Test
//    public void callDownstream_returnsErrorOnMonoEmpty() {
//        given(service.read()).willReturn(Mono.empty());
//        StepVerifier.create(strategy.callDownstream())
//                .expectError()
//                .verify();
//    }
}