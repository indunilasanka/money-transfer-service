package com.revolut.mts.dao;

import com.revolut.mts.model.Transaction;

/**
 * Handles dao operations related to the transaction api calls
 *
 * @author iasa0862 18/07/19
 */
public interface TransactionDao {

    int createNewTransaction(Transaction transactionDetails);

    void updateTransactionStatus(String status, int transactionId);

    Transaction getTransactionDetails(int transactionId);
}
