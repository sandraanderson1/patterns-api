package com.guardian.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = GuardianProps.class)
@ActiveProfiles("test")
class ApplicationPropsTest {

    @Autowired
    private GuardianProps applicationProps;

    @Test
    void returnsApiKey() {
        Assertions.assertEquals(applicationProps.getApiKey(), "test-key");
    }

    @Test
    void returnsBaseUrl() {
        Assertions.assertEquals(applicationProps.getBaseUrl(), "guardian-base-url/");
    }
}