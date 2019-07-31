package com.revolut.mts.dao;

/**
 * Handles dao operations related to the initial setup
 *
 * @author iasa0862 18/07/19
 */
public interface InitialSetupDao {

    void createUserAccountTable();

    void createTransactionTable();

}
