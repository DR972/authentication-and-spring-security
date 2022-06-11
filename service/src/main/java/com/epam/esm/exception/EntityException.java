package com.epam.esm.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The class {@code EntityException} is generated if invalid entity values are specified.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 * @see RuntimeException
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EntityException extends RuntimeException {

    /**
     * parameters that caused the EntityException exception.
     */
    private String parameters;

    public EntityException(String message, String parameters) {
        super(message);
        this.parameters = parameters;
    }
}