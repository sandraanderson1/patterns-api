package com.guardian.api.exceptions;

public class UnsupportedOperationException extends RuntimeException {

    public UnsupportedOperationException(String downstream) {
        super("Unsupported operation for API");
    }
}
