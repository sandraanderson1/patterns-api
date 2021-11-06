package com.guardian.api;

import com.guardian.api.errorHandlers.HttpErrorHandler;
import com.guardian.api.response.guardian.GuardianResponse;
import com.guardian.api.response.one.OneResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfig {

    @Bean
    public DownstreamExecutor<GuardianResponse> guardianExecutorConfig() {
        return new DownstreamExecutor<>(GuardianResponse.class);
    }

    @Bean
    public DownstreamExecutor<OneResponse> oneExecutorConfig() {
        return new DownstreamExecutor<>(OneResponse.class);
    }
}
