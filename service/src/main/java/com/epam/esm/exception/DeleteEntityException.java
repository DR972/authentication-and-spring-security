package com.epam.esm.exception;

/**
 * The class {@code DeleteEntityException} is generated in case entity already exists in database.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 * @see EntityException
 */
public class DeleteEntityException extends EntityException {

    /**
     * The constructor creates a DeleteEntityException object
     *
     * @param message String message
     * @param param   String param
     */
    public DeleteEntityException(String message, String param) {
        super(message, param);
    }
}

