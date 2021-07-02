package com.guardian.handlers;

import com.guardian.api.response.one.OneResponse;
import com.guardian.api.services.OneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
public class OneHandler {

    private OneService oneService;

    public OneHandler(OneService oneService) {
        this.oneService = oneService;
    }

    public Mono<ServerResponse> getResponse(ServerRequest serverRequest) {
        return oneService.read()
                .flatMap(s -> ServerResponse.ok().contentType(APPLICATION_JSON).body(Mono.just(s), OneResponse.class));
    }
}