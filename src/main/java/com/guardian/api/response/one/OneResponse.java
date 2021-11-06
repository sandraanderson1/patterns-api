package com.guardian.api.response.one;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.guardian.api.response.DownstreamResponse;
import lombok.Data;
import lombok.NonNull;

@Data
public class OneResponse implements DownstreamResponse {
    @JsonProperty
    @NonNull
    private String affirmation;
}
