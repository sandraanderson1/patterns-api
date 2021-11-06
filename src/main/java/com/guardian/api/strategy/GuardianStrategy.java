package com.guardian.api.strategy;

import com.guardian.api.response.guardian.GuardianClientResponse;
import com.guardian.api.services.GuardianService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GuardianStrategy implements DownstreamStrategy<GuardianClientResponse> {

    private GuardianService guardianService;

    public GuardianStrategy(GuardianService guardianService) {
        this.guardianService = guardianService;
    }

    @Override
    public Mono<GuardianClientResponse> callDownstream() {
        return guardianService.getNews();
    }

    @Override
    public Strategy getStrategy() {
        return Strategy.GUARDIAN;
    }
}
