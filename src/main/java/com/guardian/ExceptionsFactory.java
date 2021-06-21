package com.guardian;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

public class ExceptionsFactory {

    public static HttpClientErrorException createHttpClientException(Downstream downstream) {
        return new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Downstream " + downstream + " has failed");
    }

    public static HttpServerErrorException createHttpServerException(Downstream downstream) {
        return new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Downstream " + downstream + " has failed");
    }

}