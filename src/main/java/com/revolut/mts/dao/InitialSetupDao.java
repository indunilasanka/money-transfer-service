package com.revolut.mts.dao;

import java.sql.Connection;

public interface InitialSetupDao {

    void createUserAccountTable(Connection connection);

    void createTransactionTable(Connection connection);

}
