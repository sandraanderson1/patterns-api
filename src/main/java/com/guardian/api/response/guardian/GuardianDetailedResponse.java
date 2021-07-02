package com.guardian.api.response.guardian;

import lombok.Getter;

import java.util.List;

@Getter
public class GuardianDetailedResponse {
    private String status;
    private String userTier;
    private String total;
    private String startIndex;
    private String pageSize;
    private String currentPage;
    private String pages;
    private List<GuardianArticle> results;
}
