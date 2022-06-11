package com.epam.esm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The  Enum {@code ExceptionCode} presents values which will be set into {@link GlobalExceptionHandler} in case generating exceptions.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public enum ExceptionCode {
    BAD_REQUEST_EXCEPTION(40001),
    MESSAGE_NOT_READABLE_EXCEPTION(40002),
    ARGUMENT_NOT_VALID(40003),
    DUPLICATE_ENTITY_EXCEPTION(40004),
    SORT_TYPE_EXCEPTION(40005),
    NOT_FOUND_PATH_VARIABLE_EXCEPTION(40006),
    AUTHORIZED_EXCEPTION(40101),
    BAD_CREDENTIALS_EXCEPTION(40102),
    FORBIDDEN_EXCEPTION(40301),
    NOT_FOUND_URL_EXCEPTION(40401),
    NOT_FOUND_METHOD_ARGUMENT_EXCEPTION(40402),
    NO_SUCH_ENTITY_EXCEPTION(40403),
    METHOD_NOT_ALLOWED_EXCEPTION(40501),
    CONFLICT_EXCEPTION(40901),
    DELETE_ENTITY_EXCEPTION(42401),
    INTERNAL_SERVER_ERROR_EXCEPTION(50001),
    DATABASE_ERROR(50002);

    /**
     * error code.
     */
    private int errorCode;
}
