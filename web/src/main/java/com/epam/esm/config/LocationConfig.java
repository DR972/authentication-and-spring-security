package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.Locale;

/**
 * Class {@code LocationConfig} contains spring configuration for web subproject to work with locale.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Configuration
public class LocationConfig {
    /**
     * This method creates a bean that will be used to get LocaleResolver.
     *
     * @return the message source
     */
    @Bean
    public LocaleResolver localeResolver() {
        final AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setSupportedLocales(Arrays.asList(new Locale("ru"), new Locale("en")));
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    /**
     * This method creates a bean that will be used to get info from properties files.
     *
     * @return the message source
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("localization/message");
        resourceBundleMessageSource.setDefaultEncoding("UTF-8");
        resourceBundleMessageSource.setUseCodeAsDefaultMessage(true);
        return resourceBundleMessageSource;
    }
}
