package com.guardian.api.response.guardian;

import com.guardian.api.response.DownstreamResponse;
import lombok.Data;

@Data
public class GuardianResponse implements DownstreamResponse {
    private GuardianDetailedResponse response;
}
