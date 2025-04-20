package com.etms.exception;


public class ETMSException extends Exception {

    public ETMSException(String message) {
        super(message);
    }

    public ETMSException(String message, Throwable cause) {
        super(message, cause);
    }
}
