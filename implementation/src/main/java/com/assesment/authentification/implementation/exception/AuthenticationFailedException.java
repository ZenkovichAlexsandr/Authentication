package com.assesment.authentification.implementation.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationFailedException extends BusinessException {

    public AuthenticationFailedException() {
        super("Authentication failed.");
    }

    public AuthenticationFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getErrorCode() {
        return "AUTHENTICATION_FAILED";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
