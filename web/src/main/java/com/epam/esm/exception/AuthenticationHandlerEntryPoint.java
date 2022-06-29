package com.epam.esm.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.epam.esm.exception.ExceptionCode.AUTHORIZED_EXCEPTION;

/**
 * Class {@code AuthenticationHandlerEntryPoint} is implementation of interface {@link AuthenticationEntryPoint}
 * for customize authentication process.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Component
public class AuthenticationHandlerEntryPoint implements AuthenticationEntryPoint {
    /**
     * LocaleResolver localeResolver.
     */
    private final LocaleResolver localeResolver;
    /**
     * ResourceBundleMessageSource resourceBundleMessageSource.
     */
    private final ResourceBundleMessageSource resourceBundleMessageSource;

    /**
     * The constructor creates a AuthenticationHandlerEntryPoint object
     *
     * @param localeResolver              LocaleResolver localeResolver
     * @param resourceBundleMessageSource ResourceBundleMessageSource resourceBundleMessageSource
     */
    @Autowired
    public AuthenticationHandlerEntryPoint(LocaleResolver localeResolver, ResourceBundleMessageSource resourceBundleMessageSource) {
        this.localeResolver = localeResolver;
        this.resourceBundleMessageSource = resourceBundleMessageSource;
    }

    @SneakyThrows
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) {
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.getWriter().write(String.valueOf(new ObjectMapper()
                .writeValueAsString(new ApiError(AUTHORIZED_EXCEPTION, resourceBundleMessageSource.getMessage("ex.unauthorized",
                        null, localeResolver.resolveLocale(request))))));
    }
}
