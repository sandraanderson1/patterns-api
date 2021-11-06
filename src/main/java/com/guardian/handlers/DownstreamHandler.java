package com.guardian.handlers;

import com.guardian.api.response.guardian.GuardianResponse;
import com.guardian.api.response.one.OneResponse;
import com.guardian.api.strategy.GuardianStrategy;
import com.guardian.api.strategy.OneStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Locale;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
public class DownstreamHandler {

   private final GuardianStrategy guardianStrategy;
   private final OneStrategy oneStrategy;

   //one handler and based on some parameter designate to a strategy
    public DownstreamHandler(GuardianStrategy guardianStrategy, OneStrategy oneStrategy) {
        this.guardianStrategy = guardianStrategy;
        this.oneStrategy = oneStrategy;
    }

    public Mono<ServerResponse> getResponse(ServerRequest serverRequest) {
        if (serverRequest.headers().acceptLanguage().equals(Locale.LanguageRange.parse("en-GB"))) {
            return guardianStrategy.callDownstream()
                    .flatMap(s -> ServerResponse.ok().contentType(APPLICATION_JSON).body(Mono.just(s), GuardianResponse.class));
        } else {
            return oneStrategy.callDownstream()
                    .flatMap(s -> ServerResponse.ok().contentType(APPLICATION_JSON).body(Mono.just(s), OneResponse.class));
        }
    }
    //downstream strategy getLanguageRange
    //map of strategies/strategy config (spring)
    //map that has the header and strategy that applies
}