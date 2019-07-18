package com.revolut.mts.dao;

import com.revolut.mts.model.Transaction;

import java.sql.Connection;

public interface TransactionDao {

    int createNewTransaction(Connection connection, Transaction transactionDetails);

    void updateTransactionStatus(Connection connection, String status, int transactionId);

    Transaction getTransactionDetails(Connection connection, int transactionId);
}
