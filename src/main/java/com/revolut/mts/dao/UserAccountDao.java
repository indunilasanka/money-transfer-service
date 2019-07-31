package com.revolut.mts.dao;

import com.revolut.mts.model.UserAccount;

import java.util.List;

/**
 * Handles dao operations related to the user account api calls
 *
 * @author iasa0862 18/07/19
 */
public interface UserAccountDao {

    int createNewUserAccount(UserAccount userAccountDetails);

    void updateUserAccountDetails(UserAccount updatedUserAccountDetails, int accountNumber);

    void updateUserAccountBalance(int accountNumber, double amount);

    void deleteUserAccount(int accountNumber);

    UserAccount getUserAccountDetails(int accountNumber);

    List<UserAccount> getAllAccountDetails();
}
