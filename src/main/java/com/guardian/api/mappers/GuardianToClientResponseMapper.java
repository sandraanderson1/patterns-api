package com.guardian.api.mappers;

import com.guardian.api.dataModels.UserData;
import com.guardian.api.response.guardian.GuardianClientResponse;
import com.guardian.api.response.guardian.GuardianResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class GuardianToClientResponseMapper implements Function<GuardianResponse, GuardianClientResponse> {

    @Override
    public GuardianClientResponse apply(GuardianResponse guardianResponse) {
        UserData userData = new UserData.Builder(guardianResponse.getResponse().getUserTier()).build();
        return new GuardianClientResponse(userData, guardianResponse.getResponse().getStatus(), guardianResponse.getResponse().getResults());
    }
}
