package com.assesment.authentification.implementation.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessExceptionErrorInfo {
    @NonNull
    private String code;

    @NonNull
    private String message;

    public BusinessExceptionErrorInfo(final BusinessException exception) {
        this.code = exception.getErrorCode();
        this.message = exception.getMessage();
    }
}
