package com.epam.esm.exception;

/**
 * The class {@code SortValueException} is generated if incorrect sorting types are specified.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 * @see EntityException
 */
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
