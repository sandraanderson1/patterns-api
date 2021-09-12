package com.guardian.handlers;

import com.guardian.api.response.one.OneResponse;
import com.guardian.api.services.OneService;
import com.guardian.api.strategy.OneStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
public class OneHandler {

    private final OneStrategy oneStrategy;

    public OneHandler(OneStrategy oneStrategy) {
        this.oneStrategy = oneStrategy;
    }

    public Mono<ServerResponse> getResponse(ServerRequest serverRequest) {
        return oneStrategy.callDownstream()
                .flatMap(s -> ServerResponse.ok().contentType(APPLICATION_JSON)
                        .header("X-Random-Header", "BoringOne" )
                        .body(Mono.just(s), OneResponse.class));
    }
}