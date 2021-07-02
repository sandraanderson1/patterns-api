package com.guardian.api;

import com.guardian.handlers.GuardianHandler;
import com.guardian.handlers.OneHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> route(GuardianHandler guardianHandler, OneHandler oneHandler) {

        return RouterFunctions
                .route(GET("/guardian"), guardianHandler::getResponse)
                .andRoute(GET("/one"), oneHandler::getResponse);
    }

}
