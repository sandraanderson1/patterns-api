package com.guardian.api.strategy;

import com.guardian.api.response.one.OneClientResponse;
import com.guardian.api.services.OneService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class OneStrategy implements DownstreamStrategy<OneClientResponse> {

    private OneService oneService;

    public OneStrategy(OneService oneService) {
        this.oneService = oneService;
    }

    @Override
    public Mono<OneClientResponse> callDownstream() {
        return oneService.getNews();
    }

    @Override
    public Strategy getStrategy() {
        return Strategy.ONE;
    }
}
