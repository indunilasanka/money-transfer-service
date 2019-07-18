package com.revolut.mts.exception;

/**
 * Customized exception to handle transaction failures
 *
 * @author iasa0862 18/07/19
 */
public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(String message) {
        super(message);
    }

    public TransactionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
