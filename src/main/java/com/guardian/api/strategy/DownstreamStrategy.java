package com.guardian.api.strategy;

import reactor.core.publisher.Mono;

public interface DownstreamStrategy<T> {

    Mono<T> callDownstream();

    Strategy getStrategy();
}
