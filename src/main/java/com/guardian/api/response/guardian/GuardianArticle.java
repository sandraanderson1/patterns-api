package com.guardian.api.response.guardian;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class GuardianArticle {
    private String id;
    private String type;
    private String webTitle;
    private String webUrl;
    private String apiUrl;
    private String description;
    @JsonIgnore
    private String activeSponsorships;
    private String paidContentType;
}
