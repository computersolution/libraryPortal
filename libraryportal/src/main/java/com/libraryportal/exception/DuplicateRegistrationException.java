package com.libraryportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * DuplicateRegistrationException is a custom exception class that represents the scenario where a registration is duplicated.
 * It is annotated with @ResponseStatus to automatically return a BAD_REQUEST status code when thrown.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateRegistrationException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new DuplicateRegistrationException with the specified detail message.
     *
     * @param message The detail message.
     */
    public DuplicateRegistrationException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new DuplicateRegistrationException with the specified detail message and cause.
     * 
     * @param message the detail message.
     * @param cause the cause.
     */
    public DuplicateRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
