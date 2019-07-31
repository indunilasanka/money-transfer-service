package com.revolut.mts.service;

import com.revolut.mts.dao.UserAccountDao;
import com.revolut.mts.dao.impl.UserAccountDaoImpl;
import com.revolut.mts.exception.AccountNotFoundException;
import com.revolut.mts.model.Status;
import com.revolut.mts.model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.revolut.mts.common.Constants.ErrorMessages.ACCOUNT_NOT_FOUND;
import static com.revolut.mts.common.Constants.ErrorMessages.NO_ACCOUNTS;
import static com.revolut.mts.common.Constants.StatusCodes.EXISTING_ACCOUNT_DELETE_CODE;
import static com.revolut.mts.common.Constants.StatusCodes.EXISTING_ACCOUNT_UPDATE_CODE;
import static com.revolut.mts.common.Constants.StatusCodes.NEW_ACCOUNT_CREATED_CODE;
import static com.revolut.mts.common.Constants.StatusMessages.SUCCESSFULLY_CREATED_NEW_ACCOUNT;
import static com.revolut.mts.common.Constants.StatusMessages.SUCCESSFULLY_DELETED_EXISTING_ACCOUNT;
import static com.revolut.mts.common.Constants.StatusMessages.SUCCESSFULLY_UPDATED_EXISTING_ACCOUNT;

/**
 * Handles the business logic of user account operations
 *
 * @author iasa0862 18/07/19
 */
public class UserAccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountService.class);
    private UserAccountDao userAccountDao;

    public UserAccountService() {
        userAccountDao = new UserAccountDaoImpl();
    }

    public List<UserAccount> getAllUserAccounts() {
        List<UserAccount> userAccounts = userAccountDao.getAllAccountDetails();
        if (userAccounts.isEmpty()) {
            LOGGER.error("No user accounts available");
            throw new AccountNotFoundException(NO_ACCOUNTS);
        }
        return userAccounts;
    }

    public UserAccount getUserAccountDetails(int accountNumber) {
        UserAccount retrievedUserAccount = userAccountDao.getUserAccountDetails(accountNumber);
        if (retrievedUserAccount == null) {
            LOGGER.error("No user account details available for the given account number");
            throw new AccountNotFoundException(String.format(ACCOUNT_NOT_FOUND, accountNumber));
        }
        return retrievedUserAccount;
    }

    public Status createNewUserAccount(UserAccount newUserAccount) {
        int newAccountNumber = userAccountDao.createNewUserAccount(newUserAccount);
        return new Status(String.format(SUCCESSFULLY_CREATED_NEW_ACCOUNT, newAccountNumber), NEW_ACCOUNT_CREATED_CODE);
    }

    public Status updateExistingUserAccount(UserAccount updatedUserAccount, int accountNumber) {
        getUserAccountDetails(accountNumber);
        userAccountDao.updateUserAccountDetails(updatedUserAccount, accountNumber);
        return new Status(String.format(SUCCESSFULLY_UPDATED_EXISTING_ACCOUNT, updatedUserAccount.getAccountNumber()), EXISTING_ACCOUNT_UPDATE_CODE);
    }

    public Status deleteExistingUserAccount(int accountNumber) {
        getUserAccountDetails(accountNumber);
        userAccountDao.deleteUserAccount(accountNumber);
        return new Status(String.format(SUCCESSFULLY_DELETED_EXISTING_ACCOUNT, accountNumber), EXISTING_ACCOUNT_DELETE_CODE);
    }

}
