package com.assesment.authentification.implementation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Configuration
@ConditionalOnClass(ControllerAdvice.class)
public class ExceptionHandlingAutoConfig {

    @Slf4j
    @ControllerAdvice
    public static class BusinessExceptionHandler {

        /**
         * Handler to catch {@link BusinessException} and it's children and transform them to readable message for the
         * client in form of {@link BusinessExceptionErrorInfo}.
         *
         * @param exception - exception that occurred and should be processed
         * @return client readable error
         */
        @ResponseStatus(value = HttpStatus.BAD_REQUEST)
        @ExceptionHandler({Exception.class})
        @ResponseBody
        public ResponseEntity<BusinessExceptionErrorInfo> handle(final Exception exception) {
            // log error message
            log.error("Error during execution.", exception);

            if (exception instanceof AccessDeniedException) {
                throw (AccessDeniedException) exception;
            }
            if (exception instanceof AuthenticationException) {
                throw (AuthenticationException) exception;
            }
            if (exception instanceof MethodArgumentNotValidException) {
                return handleException((MethodArgumentNotValidException) exception);
            }
            if (exception instanceof BusinessException) {
                return handleException((BusinessException) exception);
            }
            return handleException(new BusinessException(exception.getMessage(), exception));
        }

        private ResponseEntity<BusinessExceptionErrorInfo> handleException(
                final MethodArgumentNotValidException exception) {
            final StringBuilder messageBuilder = new StringBuilder();

            exception.getBindingResult()
                    .getAllErrors()
                    .forEach(error -> messageBuilder.append("\r\n").append(error.getDefaultMessage()));

            return new ResponseEntity<>(
                    new BusinessExceptionErrorInfo("VALIDATION_FAILED", messageBuilder.toString()),
                    new HttpHeaders(),
                    HttpStatus.BAD_REQUEST
            );
        }

        private ResponseEntity<BusinessExceptionErrorInfo> handleException(final BusinessException exception) {
            return new ResponseEntity<>(
                    new BusinessExceptionErrorInfo(exception),
                    new HttpHeaders(),
                    exception.getHttpStatus()
            );
        }
    }
}
