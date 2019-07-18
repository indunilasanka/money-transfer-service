package com.revolut.mts.dao.impl;

import com.revolut.mts.dao.UserAccountDao;
import com.revolut.mts.exception.DatabaseException;
import com.revolut.mts.exception.TransactionFailureException;
import com.revolut.mts.model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.revolut.mts.common.Constants.ErrorMessages.CONSTRAINT_VIOLATION_ERROR;
import static com.revolut.mts.common.Constants.ErrorMessages.INCORRECT_BALANCE_AMOUNT;

public class UserAccountDaoImpl implements UserAccountDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountDaoImpl.class);

    @Override
    public int createNewUserAccount(Connection connection, UserAccount userAccountDetails) {
        int newAccountId;

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO user_accounts(account_holder, balance) values (?,?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userAccountDetails.getAccountHolder());
            statement.setDouble(2, userAccountDetails.getBalance());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                newAccountId = resultSet.getInt(1);
            } else {
                LOGGER.error("Error occurred when inserting new user account");
                throw new DatabaseException("Error occurred when inserting new user account");
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            if (e.getMessage().contains(CONSTRAINT_VIOLATION_ERROR)) {
                LOGGER.error("Constraint violation when inserting new user account", e);
                throw new TransactionFailureException(INCORRECT_BALANCE_AMOUNT);
            } else {
                LOGGER.error("Error occurred when inserting new user account", e);
                throw new DatabaseException("Error occurred when inserting new user account");
            }
        }

        return newAccountId;
    }

    @Override
    public void updateUserAccountDetails(Connection connection, UserAccount updatedUserAccountDetails) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE user_accounts SET account_holder = ?, balance = ? WHERE account_number = ?");
            statement.setString(1, updatedUserAccountDetails.getAccountHolder());
            statement.setDouble(2, updatedUserAccountDetails.getBalance());
            statement.setInt(3, updatedUserAccountDetails.getAccountNumber());
            statement.executeUpdate();

            statement.close();
            connection.close();
        } catch (SQLException e) {

            if (e.getMessage().contains(CONSTRAINT_VIOLATION_ERROR)) {
                LOGGER.error("Constraint violation when updating existing user account", e);
                throw new TransactionFailureException(INCORRECT_BALANCE_AMOUNT);
            } else {
                LOGGER.error("Error occurred when updating existing user account", e);
                throw new DatabaseException("Error occurred when updating existing user account");
            }
        }
    }

    @Override
    public void updateUserAccountBalance(Connection connection, int accountNumber, double amount) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE user_accounts SET balance = balance + (?) WHERE account_number = ?");
            statement.setDouble(1, amount);
            statement.setInt(2, accountNumber);
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {

            if (e.getMessage().contains(CONSTRAINT_VIOLATION_ERROR)) {
                LOGGER.error("Constraint violation when updating account balance of the existing user account", e);
                throw new TransactionFailureException(INCORRECT_BALANCE_AMOUNT);
            } else {
                LOGGER.error("Error occurred when updating account balance of the existing user account", e);
                throw new DatabaseException("Error occurred when updating account balance of the existing user account");
            }
        }
    }

    @Override
    public void deleteUserAccount(Connection connection, int accountNumber) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM user_accounts WHERE account_number = ?");
            statement.setInt(1, accountNumber);
            statement.executeUpdate();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("Error occurred when deleting existing user account", e);
            throw new DatabaseException("Error occurred when deleting existing user account");
        }
    }

    @Override
    public UserAccount getUserAccountDetails(Connection connection, int accountNumber) {
        UserAccount retrievedUserAccount = null;

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT account_number,account_holder,balance FROM user_accounts WHERE account_number = ?");
            statement.setInt(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                retrievedUserAccount = new UserAccount();
                retrievedUserAccount.setAccountNumber(resultSet.getInt("account_number"));
                retrievedUserAccount.setAccountHolder(resultSet.getString("account_holder"));
                retrievedUserAccount.setBalance(resultSet.getDouble("balance"));
            }

            statement.close();
            connection.close();
        } catch (Exception e) {
            LOGGER.error("Error occurred when retrieving user account details", e);
            throw new DatabaseException("Error occurred when retrieving user account details");
        }

        return retrievedUserAccount;
    }

    @Override
    public List<UserAccount> getAllAccountDetails(Connection connection) {
        List<UserAccount> userAccounts = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT account_number,account_holder,balance FROM user_accounts");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UserAccount retrievedUserAccount = new UserAccount();
                retrievedUserAccount.setAccountNumber(resultSet.getInt("account_number"));
                retrievedUserAccount.setAccountHolder(resultSet.getString("account_holder"));
                retrievedUserAccount.setBalance(resultSet.getDouble("balance"));
                userAccounts.add(retrievedUserAccount);
            }

            statement.close();
            connection.close();
        } catch (Exception e) {
            LOGGER.error("Error occurred when retrieving user account details", e);
            throw new DatabaseException("Error occurred when retrieving user account details");
        }

        return userAccounts;
    }
}
