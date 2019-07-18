package com.revolut.mts.dao;

import com.revolut.mts.model.UserAccount;

import java.sql.Connection;
import java.util.List;

public interface UserAccountDao {

    int createNewUserAccount(Connection connection, UserAccount userAccountDetails);

    void updateUserAccountDetails(Connection connection, UserAccount updatedUserAccountDetails, int accountNumber);

    void updateUserAccountBalance(Connection connection, int accountNumber, double amount);

    void deleteUserAccount(Connection connection, int accountNumber);

    UserAccount getUserAccountDetails(Connection connection, int accountNumber);

    List<UserAccount> getAllAccountDetails(Connection connection);
}
