package com.guardian;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
//        Hooks.onOperatorDebug();
        SpringApplication.run(ApiApplication.class, args);
    }
}