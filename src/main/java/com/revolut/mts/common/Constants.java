package com.revolut.mts.common;

/**
 * Maintains constants of the application
 *
 * @author iasa0862 18/07/19
 */
public class Constants {

    public static class ErrorMessages {
        public static final String ACCOUNT_NOT_FOUND = "No account details for account %s";
        public static final String TRANSACTION_NOT_FOUND = "No transaction details for transaction id %s";
        public static final String NO_ACCOUNTS = "No accounts available";
        public static final String INSUFFICIENT_ACCOUNT_BALANCE = "Transaction failed due to insufficient account balance in the account %s";
        public static final String INCORRECT_BALANCE_AMOUNT = "Balance amount should be greater than 0";
        public static final String INCORRECT_TRANSACTION_AMOUNT = "Transaction amount should be greater than 0";
        public static final String CONSTRAINT_VIOLATION_ERROR = "Check constraint violation";

        private ErrorMessages() {
        }
    }

    public static class StatusMessages {
        public static final String SUCCESSFULLY_CREATED_NEW_ACCOUNT = "Successfully created new account %s";
        public static final String SUCCESSFULLY_UPDATED_EXISTING_ACCOUNT = "Successfully updated existing account %s";
        public static final String SUCCESSFULLY_UPDATED_ACCOUNT_BALANCE = "Successfully updated account balance of the account %s";
        public static final String TRANSACTION_SUCCESSFUL = "Transaction successfull. Transaction id %s";
        public static final String SUCCESSFULLY_DELETED_EXISTING_ACCOUNT = "Successfully deleted existing account %s";
        public static final String UNEXPECTED_ERROR_OCCURRED = "Unexpected error occurred while processing the request";
        public static final String FAILED_STATUS = "FAILED";
        public static final String SUCCESS_STATUS = "SUCCESS";
    }

    public static class StatusCodes {
        public static final int NEW_ACCOUNT_CREATED_CODE = 1000;
        public static final int EXISTING_ACCOUNT_UPDATE_CODE = 1001;
        public static final int EXISTING_ACCOUNT_DELETE_CODE = 1002;
        public static final int ACCOUNT_BALANCE_UPDATE_CODE = 1003;
        public static final int TRANSACTION_SUCCESSFUL_CODE = 1004;
        public static final int ACCOUNT_NOT_FOUND_CODE = 2000;
        public static final int TRANSACTION_NOT_FOUND_CODE = 2001;
        public static final int TRANSACTION_FAILURE_CODE = 2002;
        public static final int UNEXPECTED_ERROR_CODE = 3000;
    }
}
