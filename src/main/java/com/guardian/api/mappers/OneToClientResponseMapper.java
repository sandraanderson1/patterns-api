package com.guardian.api.mappers;

import com.guardian.api.response.one.OneClientResponse;
import com.guardian.api.response.one.OneResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OneToClientResponseMapper implements Function<OneResponse, OneClientResponse> {

    @Override
    public OneClientResponse apply(OneResponse oneResponse) {
        return new OneClientResponse(oneResponse.getAffirmation());
    }
}
