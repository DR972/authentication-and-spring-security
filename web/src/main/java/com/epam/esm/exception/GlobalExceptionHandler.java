package com.epam.esm.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ExceptionCode.ARGUMENT_NOT_VALID;
import static com.epam.esm.exception.ExceptionCode.AUTHORIZED_EXCEPTION;
import static com.epam.esm.exception.ExceptionCode.BAD_CREDENTIALS_EXCEPTION;
import static com.epam.esm.exception.ExceptionCode.DATABASE_ERROR;
import static com.epam.esm.exception.ExceptionCode.DELETE_ENTITY_EXCEPTION;
import static com.epam.esm.exception.ExceptionCode.DUPLICATE_ENTITY_EXCEPTION;
import static com.epam.esm.exception.ExceptionCode.FORBIDDEN_EXCEPTION;
import static com.epam.esm.exception.ExceptionCode.INTERNAL_SERVER_ERROR_EXCEPTION;
import static com.epam.esm.exception.ExceptionCode.MESSAGE_NOT_READABLE_EXCEPTION;
import static com.epam.esm.exception.ExceptionCode.METHOD_NOT_ALLOWED_EXCEPTION;
import static com.epam.esm.exception.ExceptionCode.NOT_FOUND_METHOD_ARGUMENT_EXCEPTION;
import static com.epam.esm.exception.ExceptionCode.NOT_FOUND_URL_EXCEPTION;
import static com.epam.esm.exception.ExceptionCode.NO_SUCH_ENTITY_EXCEPTION;
import static com.epam.esm.exception.ExceptionCode.SORT_TYPE_EXCEPTION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FAILED_DEPENDENCY;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * The class {@code GlobalExceptionHandler} presents entity which will be returned from controller in case generating exceptions.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * ResourceBundleMessageSource resourceBundleMessageSource.
     */
    private final ResourceBundleMessageSource resourceBundleMessageSource;

    /**
     * The constructor creates a GlobalExceptionHandler object
     *
     * @param resourceBundleMessageSource ResourceBundleMessageSource resourceBundleMessageSource
     */
    @Autowired
    public GlobalExceptionHandler(ResourceBundleMessageSource resourceBundleMessageSource) {
        this.resourceBundleMessageSource = resourceBundleMessageSource;
    }

    /**
     * The {@code handleHttpMessageNotReadableException} method returns a response if HttpMessageNotReadableException is generated.
     *
     * @param ex      HttpMessageNotReadableException exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiError handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request, Locale locale) {
        saveLog(HttpMessageNotReadableException.class, ex, request.getParameterMap());
        return new ApiError(MESSAGE_NOT_READABLE_EXCEPTION, resourceBundleMessageSource.getMessage("ex.notReadable", null, locale));
    }

    /**
     * The {@code handleHttpRequestMethodNotSupportedException} method returns a response if HttpRequestMethodNotSupportedException is generated.
     *
     * @param ex      HttpRequestMethodNotSupportedException exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiError handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request, Locale locale, HttpServletRequest req) {
        saveLog(HttpRequestMethodNotSupportedException.class, ex, request.getParameterMap());
        if ((ex.getMethod().equals("PATCH") || ex.getMethod().equals("DELETE")) && (req.getRequestURI().equals("/certificates")
                || req.getRequestURI().equals("/certificates/") || req.getRequestURI().equals("/tags") || req.getRequestURI().equals("/tags/"))) {
            return new ApiError(ExceptionCode.NOT_FOUND_PATH_VARIABLE_EXCEPTION, resourceBundleMessageSource
                    .getMessage("ex.notSupported", new String[]{ex.getMethod()}, locale) + ". " + resourceBundleMessageSource.getMessage("ex.pathVariable", null, locale));
        } else {
            return new ApiError(METHOD_NOT_ALLOWED_EXCEPTION, resourceBundleMessageSource.getMessage("ex.notSupported", new String[]{ex.getMethod()}, locale));
        }
    }

    /**
     * The {@code handleHttpMethodArgumentNotValidException} method returns a response if MethodArgumentNotValidException is generated.
     *
     * @param ex      MethodArgumentNotValidException exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiError handleHttpMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request, Locale locale) {
        saveLog(MethodArgumentNotValidException.class, ex, request.getParameterMap());
        return new ApiError(ARGUMENT_NOT_VALID, ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> resourceBundleMessageSource.getMessage(Objects.requireNonNull(fieldError.getDefaultMessage()),
                        new String[]{String.valueOf(fieldError.getRejectedValue())}, locale)).distinct().collect(Collectors.joining("; ")));
    }

    /**
     * The {@code handleSortTypeException} method returns a response if SortValueException is generated.
     *
     * @param ex      SortValueException exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(SortValueException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiError handleSortValueException(SortValueException ex, WebRequest request, Locale locale) {
        saveLog(SortValueException.class, ex, request.getParameterMap());
        return new ApiError(SORT_TYPE_EXCEPTION, resourceBundleMessageSource.getMessage(ex.getLocalizedMessage(),
                new String[]{ex.getParameters()}, locale));
    }

    /**
     * The {@code handleHttpDuplicateEntityException} method returns a response if DuplicateEntityException is generated.
     *
     * @param ex      DuplicateEntityException exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiError handleHttpDuplicateEntityException(DuplicateEntityException ex, WebRequest request, Locale locale) {
        saveLog(DuplicateEntityException.class, ex, request.getParameterMap());
        return new ApiError(DUPLICATE_ENTITY_EXCEPTION, resourceBundleMessageSource.getMessage(ex.getLocalizedMessage(),
                new String[]{ex.getParameters()}, locale));
    }

    /**
     * The {@code handleConstraintViolationException} method returns a response if ConstraintViolationException is generated.
     *
     * @param ex      ConstraintViolationException exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiError handleConstraintViolationException(ConstraintViolationException ex, WebRequest request, Locale locale) {
        saveLog(ConstraintViolationException.class, ex, request.getParameterMap());
        return new ApiError(ARGUMENT_NOT_VALID, ex.getConstraintViolations().stream()
                .map(fieldError -> resourceBundleMessageSource.getMessage(Objects.requireNonNull(fieldError.getMessage()),
                        new String[]{String.valueOf(fieldError.getInvalidValue())}, locale)).distinct().collect(Collectors.joining("; ")));
    }

    /**
     * The {@code handleHttpAuthenticationException} method returns a response if AuthenticationException is generated.
     *
     * @param ex      AuthenticationException exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(UNAUTHORIZED)
    protected ApiError handleHttpAuthenticationException(AuthenticationException ex, WebRequest request, Locale locale) {
        saveLog(AuthenticationException.class, ex, request.getParameterMap());
        return new ApiError(AUTHORIZED_EXCEPTION, resourceBundleMessageSource.getMessage(ex.getLocalizedMessage(), null, locale));
    }

    /**
     * The {@code handleHttpBadCredentialsException} method returns a response if BadCredentialsException is generated.
     *
     * @param ex      BadCredentialsException exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(UNAUTHORIZED)
    protected ApiError handleHttpBadCredentialsException(BadCredentialsException ex, WebRequest request, Locale locale) {
        saveLog(BadCredentialsException.class, ex, request.getParameterMap());
        return new ApiError(BAD_CREDENTIALS_EXCEPTION, resourceBundleMessageSource.getMessage("ex.badUser", null, locale));
    }

    /**
     * The {@code handleHttpAccessDeniedException} method returns a response if AccessDeniedException is generated.
     *
     * @param ex      AccessDeniedException exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    protected ApiError handleHttpAccessDeniedException(AccessDeniedException ex, WebRequest request, Locale locale) {
        saveLog(AccessDeniedException.class, ex, request.getParameterMap());
        return new ApiError(FORBIDDEN_EXCEPTION, resourceBundleMessageSource.getMessage("ex.forbidden", null, locale));
    }

    /**
     * The {@code handleHttpDeleteEntityException} method returns a response if DeleteEntityException is generated.
     *
     * @param ex      DeleteEntityException exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(DeleteEntityException.class)
    @ResponseStatus(FAILED_DEPENDENCY)
    protected ApiError handleHttpDeleteEntityException(DeleteEntityException ex, WebRequest request, Locale locale) {
        saveLog(DeleteEntityException.class, ex, request.getParameterMap());
        return new ApiError(DELETE_ENTITY_EXCEPTION, resourceBundleMessageSource.getMessage(ex.getLocalizedMessage(),
                new String[]{ex.getParameters()}, locale));
    }

    /**
     * The {@code handleHttpNoHandlerFoundException} method returns a response if NoHandlerFoundException is generated.
     *
     * @param ex      NoHandlerFoundException exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(NOT_FOUND)
    protected ApiError handleHttpNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request, Locale locale) {
        saveLog(NoHandlerFoundException.class, ex, request.getParameterMap());
        return new ApiError(NOT_FOUND_URL_EXCEPTION, resourceBundleMessageSource.getMessage("ex.notURL",
                new String[]{ex.getRequestURL()}, locale));
    }

    /**
     * The {@code handleHttpMethodArgumentTypeMismatchException} method returns a response if MethodArgumentTypeMismatchException is generated.
     *
     * @param ex      MethodArgumentTypeMismatchException exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(NOT_FOUND)
    protected ApiError handleHttpMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request, Locale locale) {
        saveLog(MethodArgumentTypeMismatchException.class, ex, request.getParameterMap());
        return new ApiError(NOT_FOUND_METHOD_ARGUMENT_EXCEPTION, resourceBundleMessageSource.getMessage("ex.argumentMismatch",
                new String[]{ex.getParameter().getParameterName()}, locale));
    }

    /**
     * The {@code handleHttpNoSuchEntityException} method returns a response if NoSuchEntityException is generated.
     *
     * @param ex      NoSuchEntityException exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(NoSuchEntityException.class)
    @ResponseStatus(NOT_FOUND)
    protected ApiError handleHttpNoSuchEntityException(NoSuchEntityException ex, WebRequest request, Locale locale) {
        saveLog(NoSuchEntityException.class, ex, request.getParameterMap());
        return new ApiError(NO_SUCH_ENTITY_EXCEPTION, resourceBundleMessageSource.getMessage(ex.getLocalizedMessage(),
                new String[]{ex.getParameters()}, locale));
    }

    /**
     * The {@code handleHttpDataAccessException} method returns a response if DataAccessException is generated.
     *
     * @param ex      DataAccessException exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    protected ApiError handleHttpDataAccessException(DataAccessException ex, WebRequest request, Locale locale) {
        saveLog(DataAccessException.class, ex, request.getParameterMap());
        return new ApiError(DATABASE_ERROR, resourceBundleMessageSource.getMessage("ex.database", null, locale));
    }

    /**
     * The {@code handleHttpNullPointerException} method returns a response if NullPointerException is generated.
     *
     * @param ex      NullPointerException exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    protected ApiError handleHttpNullPointerException(NullPointerException ex, WebRequest request, Locale locale) {
        saveLog(NullPointerException.class, ex, request.getParameterMap());
        return new ApiError(INTERNAL_SERVER_ERROR_EXCEPTION, resourceBundleMessageSource.getMessage("ex.error", null, locale));
    }

    /**
     * The {@code handleHttpException} method returns a response if Exception is generated.
     *
     * @param ex      Exception exception
     * @param request WebRequest request
     * @param locale  Locale locale
     * @return ApiError object
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    protected ApiError handleHttpException(Exception ex, WebRequest request, Locale locale) {
        saveLog(Exception.class, ex, request.getParameterMap());
        return new ApiError(INTERNAL_SERVER_ERROR_EXCEPTION, resourceBundleMessageSource.getMessage("ex.error", null, locale));
    }

    /**
     * The {@code saveLog} method saves a log entry in case of an error.
     *
     * @param clazz               Class<?> clazz
     * @param ex                  Exception exception
     * @param requestParameterMap Map<String, String[]> requestParameterMap
     */
    private void saveLog(Class<?> clazz, Exception ex, Map<String, String[]> requestParameterMap) {
        StringBuilder message = new StringBuilder(clazz.getName());
        for (StackTraceElement ste : ex.getStackTrace()) {
            if (ste.toString().startsWith("com.epam.esm")) {
                message.append("\n").append(ste).append(" method - ").append(ste.getMethodName());
            }
        }
        message.append("\n").append(ex.getLocalizedMessage()).append("\n").append(requestParameterMap);
        LOGGER.error(message.toString());
    }
}