package com.revolut.mts.service;

import com.revolut.mts.configuration.DatabaseConnector;
import com.revolut.mts.dao.TransactionDao;
import com.revolut.mts.dao.UserAccountDao;
import com.revolut.mts.dao.impl.TransactionDaoImpl;
import com.revolut.mts.dao.impl.UserAccountDaoImpl;
import com.revolut.mts.exception.DatabaseException;
import com.revolut.mts.exception.TransactionFailureException;
import com.revolut.mts.exception.TransactionNotFoundException;
import com.revolut.mts.model.Status;
import com.revolut.mts.model.Transaction;
import com.revolut.mts.model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static com.revolut.mts.common.Constants.ErrorMessages.INSUFFICIENT_ACCOUNT_BALANCE;
import static com.revolut.mts.common.Constants.ErrorMessages.TRANSACTION_NOT_FOUND;
import static com.revolut.mts.common.Constants.StatusCodes.TRANSACTION_SUCCESSFUL_CODE;
import static com.revolut.mts.common.Constants.StatusMessages.FAILED_STATUS;
import static com.revolut.mts.common.Constants.StatusMessages.TRANSACTION_SUCCESSFUL;

public class TransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    private TransactionDao transactionDao;
    private UserAccountDao userAccountDao;
    private UserAccountService userAccountService;

    public TransactionService() {
        transactionDao = new TransactionDaoImpl();
        userAccountDao = new UserAccountDaoImpl();
        userAccountService = new UserAccountService();
    }

    public Transaction getTransactionDetails(int transactionId) {
        Transaction retrievedTransactionDetails = transactionDao.getTransactionDetails(DatabaseConnector.getConnection(), transactionId);
        if (retrievedTransactionDetails == null) {
            LOGGER.error("No transaction details available for the given transaction id");
            throw new TransactionNotFoundException(String.format(TRANSACTION_NOT_FOUND, transactionId));
        }
        return retrievedTransactionDetails;
    }

    public Status invokeTransaction(Transaction newTransactionDetails) {
        int newTransactionId;
        UserAccount senderAccount = userAccountService.getUserAccountDetails(newTransactionDetails.getSender());
        UserAccount receiverAccount = userAccountService.getUserAccountDetails(newTransactionDetails.getReceiver());

        if (senderAccount.getBalance() < newTransactionDetails.getAmount()) {
            newTransactionDetails.setStatus(FAILED_STATUS);
            transactionDao.createNewTransaction(DatabaseConnector.getConnection(), newTransactionDetails);
            LOGGER.error("Transaction failed sue to insufficient credit balance in the sender account");
            throw new TransactionFailureException(String.format(INSUFFICIENT_ACCOUNT_BALANCE, senderAccount.getAccountNumber()));
        } else {
            try {
                Connection transactionConnection = DatabaseConnector.getConnection();
                transactionConnection.setAutoCommit(false);
                userAccountDao.updateUserAccountBalance(transactionConnection, senderAccount.getAccountNumber(), -newTransactionDetails.getAmount());
                userAccountDao.updateUserAccountBalance(transactionConnection, receiverAccount.getAccountNumber(), +newTransactionDetails.getAmount());
                newTransactionId = transactionDao.createNewTransaction(transactionConnection, newTransactionDetails);
                transactionConnection.commit();
            } catch (SQLException e) {
                LOGGER.error("Error occurred when processing the transaction");
                throw new DatabaseException("Error occurred when processing the transaction");
            }
        }
        return new Status(String.format(TRANSACTION_SUCCESSFUL, newTransactionId), TRANSACTION_SUCCESSFUL_CODE);
    }
}
