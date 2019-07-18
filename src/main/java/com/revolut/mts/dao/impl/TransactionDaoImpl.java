package com.revolut.mts.dao.impl;

import com.revolut.mts.dao.TransactionDao;
import com.revolut.mts.exception.DatabaseException;
import com.revolut.mts.exception.TransactionFailureException;
import com.revolut.mts.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.revolut.mts.common.Constants.ErrorMessages.CONSTRAINT_VIOLATION_ERROR;
import static com.revolut.mts.common.Constants.ErrorMessages.INCORRECT_TRANSACTION_AMOUNT;

public class TransactionDaoImpl implements TransactionDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionDaoImpl.class);

    @Override
    public int createNewTransaction(Connection connection, Transaction transactionDetails) {
        int newTransactionId;

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO transactions (status,sender,receiver,amount) values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, transactionDetails.getStatus());
            statement.setDouble(2, transactionDetails.getSender());
            statement.setDouble(3, transactionDetails.getReceiver());
            statement.setDouble(4, transactionDetails.getAmount());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                newTransactionId = resultSet.getInt(1);
            } else {
                LOGGER.error("Error occurred when inserting new transaction record");
                throw new DatabaseException("Error occurred when inserting new transaction record");
            }

            statement.close();
        } catch (SQLException e) {
            if (e.getMessage().contains(CONSTRAINT_VIOLATION_ERROR)) {
                LOGGER.error("Constraint violation when inserting new transaction record", e);
                throw new TransactionFailureException(INCORRECT_TRANSACTION_AMOUNT);
            } else {
                LOGGER.error("Error occurred when inserting new transaction record", e);
                throw new DatabaseException("Error occurred when inserting new transaction record");
            }
        }

        return newTransactionId;
    }

    @Override
    public void updateTransactionStatus(Connection connection, String status, int transactionId) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE transactions SET status = ? WHERE transaction_id = ?");
            statement.setString(1, status);
            statement.setInt(2, transactionId);
            statement.executeUpdate();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            if (e.getMessage().contains(CONSTRAINT_VIOLATION_ERROR)) {
                LOGGER.error("Constraint violation when updating transaction record", e);
                throw new TransactionFailureException(INCORRECT_TRANSACTION_AMOUNT);
            } else {
                LOGGER.error("Error occurred when updating transaction record", e);
                throw new DatabaseException("Error occurred when updating transaction record");
            }
        }
    }

    @Override
    public Transaction getTransactionDetails(Connection connection, int transactionId) {
        Transaction retrievedTransactionDetails = null;

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT transaction_id,status,sender,receiver,amount FROM transactions WHERE transaction_id = ?");
            statement.setInt(1, transactionId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                retrievedTransactionDetails = new Transaction();
                retrievedTransactionDetails.setTransactionId(resultSet.getInt("transaction_id"));
                retrievedTransactionDetails.setStatus(resultSet.getString("status"));
                retrievedTransactionDetails.setSender(resultSet.getInt("sender"));
                retrievedTransactionDetails.setReceiver(resultSet.getInt("receiver"));
                retrievedTransactionDetails.setAmount(resultSet.getDouble("amount"));
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("Error occurred when extracting transaction record details", e);
            throw new DatabaseException("Error occurred when extracting transaction record details");
        }

        return retrievedTransactionDetails;
    }
}
