package com.epam.esm.exception;

/**
 * The class {@code DuplicateEntityException} is generated in case entity already exists in database.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 * @see EntityException
 */
public class DuplicateEntityException extends EntityException {

    /**
     * The constructor creates a DuplicateEntityException object
     *
     * @param message String message
     * @param param   String param
     */
    public DuplicateEntityException(String message, String param) {
        super(message, param);
    }
}
