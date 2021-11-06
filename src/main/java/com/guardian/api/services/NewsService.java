package com.guardian.api.services;

import reactor.core.publisher.Mono;

public interface NewsService<T> {
    Mono<T> getNews();
}
