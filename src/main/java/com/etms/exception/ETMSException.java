package com.etms.exception;

/**
 * Custom exception class for the Event Ticket Management System.
 * This class is used to handle application-specific exceptions.
 */
public class ETMSException extends Exception {

    /**
     * Constructs a new ETMSException with the specified detail message.
     * 
     * @param message the detail message
     */
    public ETMSException(String message) {
        super(message);
    }

    /**
     * Constructs a new ETMSException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause   the cause
     */
    public ETMSException(String message, Throwable cause) {
        super(message, cause);
    }
}