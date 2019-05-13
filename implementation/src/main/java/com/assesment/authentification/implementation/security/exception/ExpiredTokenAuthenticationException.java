package com.assesment.authentification.implementation.security.exception;

import org.springframework.security.core.AuthenticationException;

public class ExpiredTokenAuthenticationException extends AuthenticationException {

    public ExpiredTokenAuthenticationException() {
        super("Authentication token is expired.");
    }
}