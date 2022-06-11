package com.epam.esm.exception;

import lombok.EqualsAndHashCode;

/**
 * The class {@code NoSuchEntityException} is generated in case entity doesn't found in database.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 * @see RuntimeException
 */
@EqualsAndHashCode(callSuper = true)
public class NoSuchEntityException extends EntityException {

    /**
     * The constructor creates a NoSuchEntityException object
     *
     * @param message String message
     * @param param   String param
     */
    public NoSuchEntityException(String message, String param) {
        super(message, param);
    }
}
