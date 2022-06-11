package com.epam.esm.exception;

import lombok.EqualsAndHashCode;

/**
 * The class {@code SortValueException} is generated if incorrect sorting types are specified.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 * @see RuntimeException
 */
@EqualsAndHashCode(callSuper = true)
public class SortValueException extends EntityException {

    /**
     * The constructor creates a SortValueException object
     *
     * @param message String message
     * @param param   String param
     */
    public SortValueException(String message, String param) {
        super(message, param);
    }
}
