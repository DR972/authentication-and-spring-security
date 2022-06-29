package com.epam.esm.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * The {@code DateHandler} class is used to set the current date and time.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Component
public class DateHandler {

    /**
     * Method for getting current date - format ISO 8601.
     *
     * @return current date of LocalDateTime type
     */
    public LocalDateTime getCurrentDate() {
        return LocalDateTime.now();
    }
}
