package com.guardian.api.response.guardian;

import com.guardian.api.dataModels.UserData;
import com.guardian.api.response.guardian.GuardianArticle;
import lombok.Getter;

import java.util.List;

@Getter
public class ClientResponse {
    private UserData userData;
    private String status;
    private List<GuardianArticle> results;

    public ClientResponse(UserData userData, String status, List<GuardianArticle> results) {
        this.userData = userData;
        this.status = status;
        this.results = results;
    }
}
