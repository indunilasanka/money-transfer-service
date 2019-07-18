package com.revolut.mts.exception;

/**
 * Customized exception to handle data layer failures
 *
 * @author iasa0862 18/07/19
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
