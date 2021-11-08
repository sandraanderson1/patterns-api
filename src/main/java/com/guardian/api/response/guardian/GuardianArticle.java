package com.guardian.api.response.guardian;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GuardianArticle {
    @JsonProperty
    private String id;
    @JsonProperty
    private String type;
    @JsonProperty
    @JsonIgnore
    private String sectionId;
    @JsonProperty
    @JsonIgnore
    private String sectionName;
    @JsonProperty
    private String webTitle;
    @JsonProperty
    private String webUrl;
    @JsonProperty
    private String apiUrl;
    @JsonProperty
    private String description;
    @JsonProperty
    @JsonIgnore
    private String activeSponsorships;
    @JsonProperty
    private String paidContentType;
}
