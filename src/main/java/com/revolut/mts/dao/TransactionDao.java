package com.revolut.mts.dao;

import com.revolut.mts.model.Transaction;

import java.sql.Connection;

/**
 * Handles dao operations related to the transaction api calls
 *
 * @author iasa0862 18/07/19
 */
public interface TransactionDao {

    int createNewTransaction(Connection connection, Transaction transactionDetails);

    void updateTransactionStatus(Connection connection, String status, int transactionId);

    Transaction getTransactionDetails(Connection connection, int transactionId);
}
