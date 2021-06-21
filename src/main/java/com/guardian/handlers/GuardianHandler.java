package com.guardian.handlers;

import com.guardian.api.services.GuardianService;
import com.guardian.api.response.guardian.GuardianResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
public class GuardianHandler {

    private GuardianService guardianService;

    public GuardianHandler(GuardianService guardianService) {
        this.guardianService = guardianService;
    }

    public Mono<ServerResponse> getNews(ServerRequest serverRequest) {
        return guardianService.read()
                .flatMap(s -> ServerResponse.ok().contentType(APPLICATION_JSON).body(Mono.just(s), GuardianResponse.class));
    }
}