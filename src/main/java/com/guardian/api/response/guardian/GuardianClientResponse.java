package com.guardian.api.response.guardian;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.guardian.api.dataModels.UserData;
import lombok.Data;

import java.util.List;

@Data
public class GuardianClientResponse {
    @JsonProperty
    private final UserData userData;
    @JsonProperty
    private final String status;
    @JsonProperty
    private final List<GuardianArticle> results;
}
