package com.revolut.mts.exception;

/**
 * Customized exception to handle transaction failures
 *
 * @author iasa0862 18/07/19
 */
public class TransactionFailureException extends RuntimeException {

    public TransactionFailureException(String message) {
        super(message);
    }

    public TransactionFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
