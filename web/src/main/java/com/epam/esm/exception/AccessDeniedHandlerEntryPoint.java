package com.epam.esm.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.epam.esm.exception.ExceptionCode.FORBIDDEN_EXCEPTION;

/**
 * Class {@code AccessDeniedHandlerEntryPoint} is implementation of interface {@link AccessDeniedHandler}
 * for issue an error about the lack of rights.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Component
public class AccessDeniedHandlerEntryPoint implements AccessDeniedHandler {
    /**
     * LocaleResolver localeResolver.
     */
    private final LocaleResolver localeResolver;
    /**
     * ResourceBundleMessageSource resourceBundleMessageSource.
     */
    private final ResourceBundleMessageSource resourceBundleMessageSource;

    /**
     * The constructor creates a AccessDeniedHandlerEntryPoint object
     *
     * @param localeResolver              LocaleResolver localeResolver
     * @param resourceBundleMessageSource ResourceBundleMessageSource resourceBundleMessageSource
     */
    @Autowired
    public AccessDeniedHandlerEntryPoint(LocaleResolver localeResolver, ResourceBundleMessageSource resourceBundleMessageSource) {
        this.localeResolver = localeResolver;
        this.resourceBundleMessageSource = resourceBundleMessageSource;
    }

    @SneakyThrows
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) {
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        response.getWriter().write(String.valueOf(new ObjectMapper()
                .writeValueAsString(new ApiError(FORBIDDEN_EXCEPTION, resourceBundleMessageSource.getMessage("ex.forbidden",
                        null, localeResolver.resolveLocale(request))))));
    }
}
