package com.guardian.api.strategy;

import com.guardian.api.response.one.OneResponse;
import com.guardian.api.services.OneService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class OneStrategy implements DownstreamStrategy {

    private OneService oneService;

    public OneStrategy(OneService oneService) {
        this.oneService = oneService;
    }

    @Override
    public Mono<OneResponse> callDownstream() {
        return oneService.read();
    }

    @Override
    public Strategy getStrategy() {
        return null;
    }
}
