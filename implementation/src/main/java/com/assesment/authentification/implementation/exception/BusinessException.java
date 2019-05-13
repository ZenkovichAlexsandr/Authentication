package com.assesment.authentification.implementation.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    public BusinessException(final String message) {
        super(message);
    }

    public BusinessException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Error code that describes the exception.
     *
     * @return code of the error.
     */
    public String getErrorCode() {
        return "UNEXPECTED";
    }

    /**
     * Http status associated with the exception.
     *
     * @return http status of the error.
     */
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
