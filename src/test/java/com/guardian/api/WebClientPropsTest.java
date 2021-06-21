package com.guardian.api;

import com.guardian.api.webClient.BaseWebClientProps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BaseWebClientProps.class)
@ActiveProfiles("test")
class WebClientPropsTest {

    @Autowired
    private BaseWebClientProps baseWebClientProps;

    @Test
    void returnsConnectTimeoutMs() {
        Assertions.assertEquals(200, baseWebClientProps.getConnectTimeoutMs());}

    @Test
    void returnsConnectionRequestTimeoutMs() { Assertions.assertEquals(100, baseWebClientProps.getConnectionRequestTimeoutMs());}

    @Test
    void returnsPoolSize() {
        Assertions.assertEquals(1500, baseWebClientProps.getPoolSize());
    }

    @Test
    void returnsTotalRequestTimeout() { Assertions.assertEquals(1200, baseWebClientProps.getTotalRequestTimeout());}

}