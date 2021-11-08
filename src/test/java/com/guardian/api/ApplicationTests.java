package com.guardian.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardian.api.response.guardian.GuardianResponse;
import com.guardian.api.response.one.OneResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static java.nio.file.Files.readAllBytes;

public class ApplicationTests {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String mockGuardianResponse() throws IOException {
        return new String(readAllBytes(Paths.get("src/test/resources/response.json")));
    }

    public static GuardianResponse mockGuardianResponseObj() throws IOException {
        return objectMapper.readValue(new File("src/test/resources/response.json"), GuardianResponse.class);
    }

    public static String mockOneResponse() throws JsonProcessingException {
        return objectMapper.writeValueAsString("response");
    }

    public static OneResponse mockOneResponseObj() throws IOException {
        return objectMapper.readValue(mockOneResponse(), OneResponse.class);
    }
}
