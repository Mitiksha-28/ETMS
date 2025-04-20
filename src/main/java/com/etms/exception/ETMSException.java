package com.etms.exception;

/**
 * This class is used to handle application-specific exceptions.
 */
public class ETMSException extends Exception {

    /**
     * Constructs a new ETMSException with the specified detail message.
     */
    public ETMSException(String message) {
        super(message);
    }

    /**
     * Constructs a new ETMSException with the specified detail message and cause.
     */
    public ETMSException(String message, Throwable cause) {
        super(message, cause);
    }
}
