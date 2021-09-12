package com.guardian.handlers;

import com.guardian.api.response.guardian.GuardianResponse;
import com.guardian.api.strategy.GuardianStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
public class GuardianHandler {

   private final GuardianStrategy strategy;

    public GuardianHandler(GuardianStrategy strategy) {
        this.strategy = strategy;
    }

    public Mono<ServerResponse> getResponse(ServerRequest serverRequest) {
        return strategy.callDownstream()
                .flatMap(s -> ServerResponse.ok().contentType(APPLICATION_JSON).body(Mono.just(s), GuardianResponse.class));
    }
}