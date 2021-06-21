package com.guardian.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = ApplicationProps.class)
@ActiveProfiles("test")
class ApplicationPropsTest {

    @Autowired
    private ApplicationProps applicationProps;

    @Test
    void returnsApiKey() {
        assertEquals(applicationProps.getApiKey(), "test-key");
    }

    @Test
    void returnsBaseUrl() {
        assertEquals(applicationProps.getBaseUrl(), "base-url");
    }
}