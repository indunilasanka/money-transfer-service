package com.revolut.mts.exception;

/**
 * Customized exception to handle user account failures
 *
 * @author iasa0862 18/07/19
 */
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
