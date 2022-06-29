package com.epam.esm.exception;

import lombok.Data;

/**
 * Class {@code ApiError} represents objects that will be returned as a response when an error is generated.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Data
public class ApiError {
    /**
     * error message.
     */
    private String errorMessage;
    /**
     * error code.
     */
    private int errorCode;

    /**
     * The constructor creates an ApiError object
     *
     * @param exceptionCode ExceptionCode exceptionCode
     * @param errorMessage  String errorMessage
     */
    public ApiError(ExceptionCode exceptionCode, String errorMessage) {
        this.errorMessage = errorMessage;
        errorCode = exceptionCode.getErrorCode();
    }
}

