package com.guardian.api.response.guardian;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GuardianDetailedResponse {
    @JsonProperty
    private String status;
    @JsonProperty
    private String userTier;
    @JsonProperty
    private String total;
    @JsonProperty
    private String startIndex;
    @JsonProperty
    private String pageSize;
    @JsonProperty
    private String currentPage;
    @JsonProperty
    private String pages;
    @JsonProperty
    private List<GuardianArticle> results;
}
