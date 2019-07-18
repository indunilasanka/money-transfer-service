package com.revolut.mts.configuration;

import com.revolut.mts.exception.DatabaseException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnector.class);
    private static DataSource dataSource;

    public static void initializeDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:mem:mts_database");
        config.setMaximumPoolSize(10);
        config.validate();
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() {
        try {
            if (dataSource == null) {
                initializeDataSource();
            }
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Error occurred when getting the db connection", e);
            throw new DatabaseException("Error occurred when getting the db connection");
        }
    }
}