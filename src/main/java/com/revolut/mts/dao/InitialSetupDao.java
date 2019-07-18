package com.revolut.mts.dao;

import java.sql.Connection;

/**
 * Handles dao operations related to the initial setup
 *
 * @author iasa0862 18/07/19
 */
public interface InitialSetupDao {

    void createUserAccountTable(Connection connection);

    void createTransactionTable(Connection connection);

}
