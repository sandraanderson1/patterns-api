package com.guardian.api.response.one;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OneClientResponse {
    @JsonProperty
    private final String result;
}
